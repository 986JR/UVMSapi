//J. R
//API Utility Functions for Authenticated Requests
//Handles JWT token attachment, error responses, and common API operations


// Base API URL - adjust this to match your Spring Boot server
const BASE_API_URL = 'https://uvmsapiv1.onrender.com/api';

/**
 * Get JWT token from localStorage
 * @returns {string|null} JWT token or null if not found
 */
function getAuthToken() {
    // Get the token from localStorage (where it's stored after login)
    const token = localStorage.getItem('jwtToken');

    if (!token) {
        console.error('No JWT token found in localStorage');
        return null;
    }

    return token;
}

/**
 * Create headers with JWT authentication
 * @returns {Object} Headers object with Authorization and Content-Type
 */
function createAuthHeaders() {
    const token = getAuthToken();

    if (!token) {
        throw new Error('Authentication token not found. Please login again.');
    }

    return {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    };
}

/**
 * Make authenticated GET request
 * @param {string} endpoint - API endpoint (e.g., '/vendors/dashboard')
 * @returns {Promise<Object>} Promise that resolves to response data
 */
async function authenticatedGet(endpoint) {
    try {
        // Create full URL
        const url = `${BASE_API_URL}${endpoint}`;

        // Create headers with JWT token
        const headers = createAuthHeaders();

        // Make the request
        const response = await fetch(url, {
            method: 'GET',
            headers: headers
        });

        // Check if response is successful
        if (!response.ok) {
            await handleErrorResponse(response);
            return null; // This line won't execute due to throw in handleErrorResponse
        }

        // Parse and return JSON data
        const data = await response.json();
        console.log('✅ GET request successful:', endpoint, data);
        return data;

    } catch (error) {
        console.error('❌ GET request failed:', endpoint, error.message);
        throw error; // Re-throw so calling function can handle it
    }
}

/**
 * Make authenticated POST request
 * @param {string} endpoint - API endpoint
 * @param {Object} data - Data to send in request body
 * @returns {Promise<Object>} Promise that resolves to response data
 */
async function authenticatedPost(endpoint, data) {
    try {
        const url = `${BASE_API_URL}${endpoint}`;
        const headers = createAuthHeaders();

        const response = await fetch(url, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            await handleErrorResponse(response);
            return null;
        }

        const responseData = await response.json();
        console.log('✅ POST request successful:', endpoint, responseData);
        return responseData;

    } catch (error) {
        console.error('❌ POST request failed:', endpoint, error.message);
        throw error;
    }
}

/**
 * Make authenticated PUT request
 * @param {string} endpoint - API endpoint
 * @param {Object} data - Data to send in request body
 * @returns {Promise<Object>} Promise that resolves to response data
 */
async function authenticatedPut(endpoint, data) {
    try {
        const url = `${BASE_API_URL}${endpoint}`;
        const headers = createAuthHeaders();

        const response = await fetch(url, {
            method: 'PUT',
            headers: headers,
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            await handleErrorResponse(response);
            return null;
        }

        const responseData = await response.json();
        console.log('✅ PUT request successful:', endpoint, responseData);
        return responseData;

    } catch (error) {
        console.error('❌ PUT request failed:', endpoint, error.message);
        throw error;
    }
}

/**
 * Handle error responses from API
 * @param {Response} response - Fetch response object
 * @throws {Error} Throws descriptive error based on status code
 */
async function handleErrorResponse(response) {
    const status = response.status;
    let errorMessage = 'Unknown error occurred';

    try {
        // Try to get error message from response body
        const errorText = await response.text();
        errorMessage = errorText || `HTTP ${status} Error`;
    } catch (e) {
        errorMessage = `HTTP ${status} Error`;
    }

    // Handle specific error cases
    switch (status) {
        case 401:
            // Unauthorized - token might be expired or invalid
            console.error('🔐 Authentication failed. Token might be expired.');
            handleAuthenticationError();
            throw new Error('Authentication failed. Please login again.');

        case 403:
            // Forbidden - user doesn't have permission
            throw new Error('Access denied. You do not have permission to access this resource.');

        case 404:
            // Not found
            throw new Error('Resource not found. The requested data does not exist.');

        case 400:
            // Bad request - validation error
            throw new Error(`Bad request: ${errorMessage}`);

        case 500:
            // Internal server error
            throw new Error('Server error occurred. Please try again later.');

        default:
            throw new Error(`Request failed: ${errorMessage}`);
    }
}

/**
 * Handle authentication errors (expired token, etc.)
 * Clears stored token and optionally redirects to login
 */
function handleAuthenticationError() {
    // Clear the invalid token
    localStorage.removeItem('jwtToken');

    // Optional: Redirect to login page
    // window.location.href = '/login.html';

    // Or show a message to user
    console.log('🚨 Session expired. Please login again.');
}

/**
 * Check if user is authenticated (has valid token)
 * Note: This only checks if token exists, not if it's valid
 * @returns {boolean} True if token exists, false otherwise
 */
function isAuthenticated() {
    const token = getAuthToken();
    return token !== null;
}

/**
 * Logout function - clears token and redirects
 */
function logout() {
    localStorage.removeItem('jwtToken');
    console.log('🚪 User logged out');
    // window.location.href = '/login.html'; // Uncomment to redirect
}

/**
 * Display error message to user
 * @param {string} message - Error message to display
 * @param {string} containerId - ID of container to show error in
 */
function displayError(message, containerId = 'error-container') {
    const errorContainer = document.getElementById(containerId);
    if (errorContainer) {
        errorContainer.innerHTML = `
            <div class="alert alert-danger" role="alert">
                <strong>Error:</strong> ${message}
            </div>
        `;
        errorContainer.style.display = 'block';
    } else {
        // Fallback to alert if no error container found
        alert('Error: ' + message);
    }
}

/**
 * Clear error messages
 * @param {string} containerId - ID of container to clear
 */
function clearError(containerId = 'error-container') {
    const errorContainer = document.getElementById(containerId);
    if (errorContainer) {
        errorContainer.innerHTML = '';
        errorContainer.style.display = 'none';
    }
}

// Export functions for use in other files (if using modules)
// Uncomment if you're using ES6 modules:
/*
export {
    authenticatedGet,
    authenticatedPost,
    authenticatedPut,
    isAuthenticated,
    logout,
    displayError,
    clearError
};
*/
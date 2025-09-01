/**
 * Dashboard API Functions
 * Specific functions to fetch and manage dashboard data
 * Depends on api-utils.js for authenticated requests
 */

/**
 * Fetch dashboard data from backend
 * @returns {Promise<Object>} Promise that resolves to dashboard data
 */
async function fetchDashboardData() {
    try {
        console.log('🔄 Fetching dashboard data...');

        // Show loading state
        showLoadingState();

        // Make authenticated request to dashboard endpoint
        const dashboardData = await authenticatedGet('/vendors/dashboard');

        console.log('✅ Dashboard data received:', dashboardData);

        // Hide loading state
        hideLoadingState();

        return dashboardData;

    } catch (error) {
        console.error('❌ Failed to fetch dashboard data:', error.message);

        // Hide loading state
        hideLoadingState();

        // Display error to user
        displayError(error.message, 'dashboard-error');

        // Re-throw error so calling function can handle it
        throw error;
    }
}

/**
 * Alternative function to fetch dashboard data using vendor ID
 * @returns {Promise<Object>} Promise that resolves to dashboard data
 */
async function fetchDashboardDataById() {
    try {
        console.log('🔄 Fetching dashboard data by ID...');

        showLoadingState();

        // Use the alternative endpoint
        const dashboardData = await authenticatedGet('/vendors/dashboard-by-id');

        console.log('✅ Dashboard data received (by ID):', dashboardData);

        hideLoadingState();

        return dashboardData;

    } catch (error) {
        console.error('❌ Failed to fetch dashboard data by ID:', error.message);

        hideLoadingState();
        displayError(error.message, 'dashboard-error');
        throw error;
    }
}

/**
 * Initialize dashboard - fetch data and render it
 * This is the main function to call when dashboard page loads
 */
async function initializeDashboard() {
    try {
        // Clear any previous errors
        clearError('dashboard-error');

        // Check if user is authenticated
        if (!isAuthenticated()) {
            throw new Error('Not authenticated. Please login first.');
        }

        console.log('🚀 Initializing dashboard...');

        // Fetch dashboard data
        const dashboardData = await fetchDashboardData();

        // Render the data on the page (we'll create this function in Step 7)
        renderDashboardData(dashboardData);

        console.log('✅ Dashboard initialized successfully');

    } catch (error) {
        console.error('❌ Dashboard initialization failed:', error.message);

        // Handle authentication errors
        if (error.message.includes('Authentication failed') || error.message.includes('Not authenticated')) {
            // Redirect to login page or show login prompt
            handleDashboardAuthError();
        } else {
            // Show general error
            displayError('Failed to load dashboard: ' + error.message, 'dashboard-error');
        }
    }
}

/**
 * Refresh dashboard data
 * Useful for refresh buttons or periodic updates
 */
async function refreshDashboard() {
    try {
        console.log('🔄 Refreshing dashboard...');

        // Clear any existing errors
        clearError('dashboard-error');

        // Fetch fresh data
        const dashboardData = await fetchDashboardData();

        // Re-render with new data
        renderDashboardData(dashboardData);

        // Show success message (optional)
        showSuccessMessage('Dashboard refreshed successfully!');

        console.log('✅ Dashboard refreshed');

    } catch (error) {
        console.error('❌ Dashboard refresh failed:', error.message);
        displayError('Failed to refresh dashboard: ' + error.message, 'dashboard-error');
    }
}

/**
 * Handle authentication errors on dashboard
 */
function handleDashboardAuthError() {
    // Clear the dashboard content
    const dashboardContent = document.getElementById('dashboard-content');
    if (dashboardContent) {
        dashboardContent.innerHTML = `
            <div class="alert alert-warning text-center">
                <h4>⚠️ Session Expired</h4>
                <p>Your session has expired. Please login again to continue.</p>
                <button class="btn btn-primary" onclick="redirectToLogin()">
                    Go to Login
                </button>
            </div>
        `;
    }
}

/**
 * Redirect to login page
 */
function redirectToLogin() {
    // Adjust this URL to match your login page
    window.location.href = '/index.html';
}

/**
 * Show loading state on dashboard
 */
function showLoadingState() {
    // Hide dashboard content
    const dashboardContent = document.getElementById('dashboard-content');
    if (dashboardContent) {
        dashboardContent.style.display = 'none';
    }

    // Show loading spinner
    const loadingContainer = document.getElementById('loading-container');
    if (loadingContainer) {
        loadingContainer.style.display = 'block';
        loadingContainer.innerHTML = `
            <div class="text-center">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2">Loading dashboard data...</p>
            </div>
        `;
    }
}

/**
 * Hide loading state
 */
function hideLoadingState() {
    // Show dashboard content
    const dashboardContent = document.getElementById('dashboard-content');
    if (dashboardContent) {
        dashboardContent.style.display = 'block';
    }

    // Hide loading spinner
    const loadingContainer = document.getElementById('loading-container');
    if (loadingContainer) {
        loadingContainer.style.display = 'none';
    }
}

/**
 * Show success message
 * @param {string} message - Success message to display
 */
function showSuccessMessage(message) {
    const successContainer = document.getElementById('success-container');
    if (successContainer) {
        successContainer.innerHTML = `
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>✅ Success:</strong> ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
        successContainer.style.display = 'block';

        // Auto-hide after 3 seconds
        setTimeout(() => {
            successContainer.style.display = 'none';
        }, 3000);
    }
}

/**
 * Check authentication status on page load
 * Call this when dashboard page loads
 */
function checkAuthenticationStatus() {
    if (!isAuthenticated()) {
        console.log('⚠️ User not authenticated, redirecting to login');
        displayError('Please login to access the dashboard.', 'dashboard-error');

        // Optional: Auto-redirect after 3 seconds
        setTimeout(() => {
            redirectToLogin();
        }, 3000);

        return false;
    }
    return true;
}

/**
 * Setup dashboard event listeners
 * Call this when DOM is loaded
 */
function setupDashboardEventListeners() {
    // Refresh button click handler
    const refreshBtn = document.getElementById('refresh-dashboard-btn');
    if (refreshBtn) {
        refreshBtn.addEventListener('click', refreshDashboard);
    }

    // Logout button click handler
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            if (confirm('Are you sure you want to logout?')) {
                logout();
                redirectToLogin();
            }
        });
    }

    console.log('✅ Dashboard event listeners setup complete');
}

/**
 * Main dashboard startup function
 * Call this when the dashboard page loads
 */
function startDashboard() {
    console.log('🌟 Starting dashboard application...');

    // Check authentication first
    if (!checkAuthenticationStatus()) {
        return; // Stop if not authenticated
    }

    // Setup event listeners
    setupDashboardEventListeners();

    // Initialize dashboard data
    initializeDashboard();
}

// Auto-start dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('📄 DOM loaded, checking for dashboard page...');

    // Only start dashboard if we're on the dashboard page
    const dashboardContainer = document.getElementById('dashboard-content');
    if (dashboardContainer) {
        startDashboard();
    }
});
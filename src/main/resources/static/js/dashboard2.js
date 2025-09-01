// dashboard.js
// Step 7: Render Vendor Dashboard

// Ensure that dashboard-api.js is loaded first
document.addEventListener('DOMContentLoaded', () => {
    console.log('Dashboard page loaded');

    const refreshBtn = document.getElementById('refresh-dashboard-btn');
    const logoutBtn = document.getElementById('logout-btn');

    refreshBtn.addEventListener('click', () => {
        fetchDashboardData();
    });

    logoutBtn.addEventListener('click', () => {
        logout();
        window.location.href = '/index.html';
    });

    // Initial fetch
    fetchDashboardData();
});

async function fetchDashboardData() {
    const loadingContainer = document.getElementById('loading-container');
    const dashboardContent = document.getElementById('dashboard-content');
    const dashboardError = document.getElementById('dashboard-error');

    // Show loading spinner
    loadingContainer.style.display = 'block';
    loadingContainer.innerHTML = `<div class="loading-spinner"></div>`;
    dashboardContent.style.display = 'none';
    dashboardError.style.display = 'none';

    try {
        const data = await authenticatedGet('/vendors/dashboard');

        if (!data) {
            throw new Error('No dashboard data received');
        }

        // Populate dashboard fields
        document.getElementById('welcome-message').textContent = data.welcomeMessage;
        document.getElementById('dashboard-subtitle').textContent = `Hello, ${data.fullName}`;

        document.getElementById('profile-picture').src = data.profilePicturePath || 'images/default-profile.png';
        document.getElementById('vendor-full-name').textContent = data.fullName;
        document.getElementById('vendor-email').textContent = data.email;
        document.getElementById('account-status').textContent = data.accountStatus;
        document.getElementById('account-status').className = `info-value ${data.isActive ? 'status-active' : 'status-inactive'}`;
        document.getElementById('phone-number').textContent = data.phoneNumber;

        document.getElementById('company-name').textContent = data.companyName;
        document.getElementById('business-type').textContent = data.businessType;
        document.getElementById('tin-number').textContent = data.tinNumber;
        document.getElementById('business-address').textContent = data.businessAddress;

        document.getElementById('vendor-id').textContent = data.vendorId;
        document.getElementById('registration-date').textContent = data.registrationDate;
        document.getElementById('last-login').textContent = data.lastLogin ? new Date(data.lastLogin).toLocaleString() : 'N/A';
        document.getElementById('last-login-info').textContent = data.lastLogin ? `Last login: ${new Date(data.lastLogin).toLocaleString()}` : '';

        // Hide loading and show content
        loadingContainer.style.display = 'none';
        dashboardContent.style.display = 'block';

    } catch (error) {
        console.error('Error fetching dashboard data:', error.message);
        loadingContainer.style.display = 'none';
        dashboardError.style.display = 'block';
        dashboardError.innerHTML = `
            <div class="alert alert-danger" role="alert">
                ${error.message}
            </div>
        `;
    }
}

/**
 * UVMS Super Admin Single Page Application
 * Navigation and Page Management System
 */

class SuperAdminApp {
    constructor() {
        this.currentPage = 'dashboard';
        this.pages = {};
        this.init();
    }

    init() {
        this.loadPageContents();
        this.setupEventListeners();
        
        // Check URL for current page on init
        const urlParams = new URLSearchParams(window.location.search);
        const currentPage = urlParams.get('page') || 'dashboard';
        this.showPage(currentPage);
        this.updateBreadcrumb(this.getPageTitle(currentPage));
    }

    // Load all page contents from existing HTML files
    async loadPageContents() {
        const pageFiles = [
            'dashboard.html',
            'manage-admins.html', 
            'oversee-tenders.html',
            'reports.html',
            'policies.html',
            'notifications.html'
        ];

        for (const file of pageFiles) {
            try {
                const response = await fetch(file);
                const html = await response.text();
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                
                // Extract main content
                const mainContent = doc.querySelector('.main-content') || doc.querySelector('main');
                if (mainContent) {
                    const pageName = file.replace('.html', '').replace('-', '_');
                    this.pages[pageName] = mainContent.innerHTML;
                }
            } catch (error) {
                console.warn(`Could not load ${file}:`, error);
                // Fallback content for each page
                this.loadFallbackContent();
            }
        }
    }

    loadFallbackContent() {
        // Dashboard content with modern design
        this.pages.dashboard = `
            <div class="dashboard-header mb-3">
                <div class="quick-stats-row">
                    <div class="stats-card stats-primary">
                        <div class="stats-icon">📊</div>
                        <div class="stats-content">
                            <div class="stats-number" id="totalSales">$508</div>
                            <div class="stats-label">Total Sales</div>
                            <div class="stats-change positive">+12.5%</div>
                        </div>
                    </div>
                    <div class="stats-card stats-info">
                        <div class="stats-icon">👥</div>
                        <div class="stats-content">
                            <div class="stats-number" id="totalAdmins">$387</div>
                            <div class="stats-label">Total Purchase</div>
                            <div class="stats-change positive">+8.2%</div>
                        </div>
                    </div>
                    <div class="stats-card stats-warning">
                        <div class="stats-icon">📋</div>
                        <div class="stats-content">
                            <div class="stats-number" id="totalOrders">$161</div>
                            <div class="stats-label">Total Orders</div>
                            <div class="stats-change negative">-2.1%</div>
                        </div>
                    </div>
                    <div class="stats-card stats-success">
                        <div class="stats-icon">💰</div>
                        <div class="stats-content">
                            <div class="stats-number" id="totalRevenue">$231</div>
                            <div class="stats-label">Total Revenue</div>
                            <div class="stats-change positive">+15.3%</div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="grid grid-2 mb-3">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Total Sales</h3>
                        <div class="card-actions">
                            <select class="form-control form-select" style="width: auto; font-size: 0.75rem;">
                                <option>This Month</option>
                                <option>Last Month</option>
                                <option>This Year</option>
                            </select>
                        </div>
                    </div>
                    <div class="stats-summary">
                        <div class="summary-item">
                            <span class="summary-label">Revenue</span>
                            <span class="summary-value">13,956</span>
                        </div>
                        <div class="summary-item">
                            <span class="summary-label">Orders</span>
                            <span class="summary-value">27,219</span>
                        </div>
                        <div class="summary-item">
                            <span class="summary-label">Visitors</span>
                            <span class="summary-value">63,386</span>
                        </div>
                        <div class="summary-item">
                            <span class="summary-label">Conversion</span>
                            <span class="summary-value">04,739</span>
                        </div>
                    </div>
                    <div class="chart-placeholder" style="height: 200px; background: var(--uvms-surface-dark); border-radius: 8px; margin-top: 1rem; display: flex; align-items: center; justify-content: center; color: var(--uvms-text-secondary); font-size: 0.8rem;">
                        Sales Chart Visualization
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">System Overview</h3>
                    </div>
                    <div class="overview-stats">
                        <div class="overview-item">
                            <span class="overview-label">Users</span>
                            <div class="overview-data">
                                <span class="overview-number">33,956</span>
                                <span class="overview-change positive">+4.11%</span>
                            </div>
                        </div>
                        <div class="overview-item">
                            <span class="overview-label">Projects</span>
                            <div class="overview-data">
                                <span class="overview-number">50.36%</span>
                                <span class="overview-change positive">+2.65%</span>
                            </div>
                        </div>
                    </div>
                    <div class="chart-placeholder" style="height: 150px; background: var(--uvms-surface-dark); border-radius: 8px; margin-top: 1rem; display: flex; align-items: center; justify-content: center; color: var(--uvms-text-secondary); font-size: 0.8rem;">
                        Overview Chart
                    </div>
                </div>
            </div>
            
            <div class="grid grid-3">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Recent Activity</h3>
                    </div>
                    <div class="activity-list">
                        <div class="activity-item">
                            <div class="activity-avatar">JL</div>
                            <div class="activity-content">
                                <div class="activity-title">John Larson</div>
                                <div class="activity-desc">31 Aug 2019</div>
                            </div>
                        </div>
                        <div class="activity-item">
                            <div class="activity-avatar">TS</div>
                            <div class="activity-content">
                                <div class="activity-title">Teresa Shaw</div>
                                <div class="activity-desc">13 May 2019</div>
                            </div>
                        </div>
                        <div class="activity-item">
                            <div class="activity-avatar">RU</div>
                            <div class="activity-content">
                                <div class="activity-title">Rose Underwood</div>
                                <div class="activity-desc">22 Jan 2019</div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Updates</h3>
                    </div>
                    <div class="updates-list">
                        <div class="update-item">
                            <div class="update-icon success">✓</div>
                            <div class="update-content">
                                <div class="update-title">User confirmation</div>
                                <div class="update-desc">You have successfully confirmed your account</div>
                                <div class="update-time">2 minutes ago</div>
                            </div>
                        </div>
                        <div class="update-item">
                            <div class="update-icon warning">!</div>
                            <div class="update-content">
                                <div class="update-title">Continuous evaluation</div>
                                <div class="update-desc">Add a new payment method to your account</div>
                                <div class="update-time">5 minutes ago</div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Quick Actions</h3>
                    </div>
                    <div class="quick-actions">
                        <button class="btn btn-primary btn-block" onclick="app.navigateTo('manage_admins')">Add New Admin</button>
                        <button class="btn btn-secondary btn-block" onclick="app.navigateTo('policies')">Upload Policy</button>
                        <button class="btn btn-secondary btn-block" onclick="app.navigateTo('reports')">Generate Report</button>
                    </div>
                </div>
            </div>
        `;

        // Manage Admins content
        this.pages.manage_admins = `
            <h1 class="page-title">Manage College Admins</h1>
            <div class="card mb-3">
                <div class="card-header">
                    <h2 class="card-title">Add New College Admin</h2>
                    <button class="btn btn-primary" onclick="showAddAdminModal()">Add Admin</button>
                </div>
                <div class="grid grid-3">
                    <div class="stats-card">
                        <div class="stats-number">45</div>
                        <div class="stats-label">Total Admins</div>
                    </div>
                    <div class="stats-card">
                        <div class="stats-number">42</div>
                        <div class="stats-label">Active Admins</div>
                    </div>
                    <div class="stats-card">
                        <div class="stats-number">3</div>
                        <div class="stats-label">Pending Approval</div>
                    </div>
                </div>
            </div>
        `;

        // Oversee Tenders content
        this.pages.oversee_tenders = `
            <h1 class="page-title">Oversee All Tenders</h1>
            <div class="grid grid-4 mb-3">
                <div class="stats-card">
                    <div class="stats-number">67</div>
                    <div class="stats-label">Total Tenders</div>
                </div>
                <div class="stats-card">
                    <div class="stats-number">28</div>
                    <div class="stats-label">Active Tenders</div>
                </div>
                <div class="stats-card">
                    <div class="stats-number">142</div>
                    <div class="stats-label">Pending Applications</div>
                </div>
                <div class="stats-card">
                    <div class="stats-number">89</div>
                    <div class="stats-label">Approved Applications</div>
                </div>
            </div>
        `;

        // Reports content
        this.pages.reports = `
            <h1 class="page-title">System Reports & Analytics</h1>
            <div class="card mb-3">
                <div class="card-header">
                    <h2 class="card-title">Generate Custom Report</h2>
                </div>
                <div class="grid grid-3">
                    <div class="form-group">
                        <label class="form-label">Report Type</label>
                        <select class="form-control form-select">
                            <option value="">Select Report Type</option>
                            <option value="tender-summary">Tender Summary</option>
                            <option value="application-status">Application Status</option>
                            <option value="license-overview">License Overview</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Date Range</label>
                        <select class="form-control form-select">
                            <option value="last-30-days">Last 30 Days</option>
                            <option value="last-3-months">Last 3 Months</option>
                            <option value="last-year">Last Year</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Format</label>
                        <select class="form-control form-select">
                            <option value="pdf">PDF</option>
                            <option value="excel">Excel</option>
                            <option value="csv">CSV</option>
                        </select>
                    </div>
                </div>
                <div class="mt-3">
                    <button class="btn btn-primary">Generate Report</button>
                </div>
            </div>
        `;

        // Policies content
        this.pages.policies = `
            <h1 class="page-title">University Policies & Guidelines</h1>
            <div class="card mb-3">
                <div class="card-header">
                    <h2 class="card-title">Upload New Policy</h2>
                    <button class="btn btn-primary">Upload Policy</button>
                </div>
                <div class="grid grid-3">
                    <div class="stats-card">
                        <div class="stats-number">24</div>
                        <div class="stats-label">Total Policies</div>
                    </div>
                    <div class="stats-card">
                        <div class="stats-number">22</div>
                        <div class="stats-label">Active Policies</div>
                    </div>
                    <div class="stats-card">
                        <div class="stats-number">3</div>
                        <div class="stats-label">Recent Updates</div>
                    </div>
                </div>
            </div>
        `;

        // Notifications content
        this.pages.notifications = `
            <h1 class="page-title">System Notifications</h1>
            <div class="grid grid-4 mb-3">
                <div class="stats-card">
                    <div class="stats-number">47</div>
                    <div class="stats-label">Total Notifications</div>
                </div>
                <div class="stats-card">
                    <div class="stats-number">12</div>
                    <div class="stats-label">Unread</div>
                </div>
                <div class="stats-card">
                    <div class="stats-number">3</div>
                    <div class="stats-label">Urgent</div>
                </div>
                <div class="stats-card">
                    <div class="stats-number">8</div>
                    <div class="stats-label">Today</div>
                </div>
            </div>
        `;
    }

    setupEventListeners() {
        // Navigation links
        document.addEventListener('click', (e) => {
            if (e.target.matches('[data-page]')) {
                e.preventDefault();
                const page = e.target.getAttribute('data-page');
                this.navigateTo(page);
            }
        });

        // Mobile sidebar toggle
        const sidebarToggle = document.querySelector('.sidebar-toggle');
        const sidebar = document.querySelector('.sidebar');
        const mobileOverlay = document.querySelector('.mobile-overlay');

        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', () => {
                this.toggleMobileSidebar();
            });
        }

        if (mobileOverlay) {
            mobileOverlay.addEventListener('click', () => {
                this.closeMobileSidebar();
            });
        }

        // Logout functionality
        const logoutBtn = document.querySelector('.logout-btn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => {
                this.logout();
            });
        }

        // Handle browser back/forward
        window.addEventListener('popstate', (e) => {
            if (e.state && e.state.page) {
                this.showPage(e.state.page, false);
            }
        });
    }

    navigateTo(page) {
        this.showPage(page, true);
        this.closeMobileSidebar();
    }

    showPage(page, updateHistory = true) {
        // Hide all page contents
        const contentArea = document.querySelector('.content-area');
        if (!contentArea) return;

        // Show loading spinner
        contentArea.innerHTML = '<div class="loading-container"><div class="spinner"></div><p class="loading-text">Loading ' + this.getPageTitle(page).toLowerCase() + '...</p></div>';

        // Simulate loading delay for better UX
        setTimeout(() => {
            // Update content
            if (this.pages[page]) {
                contentArea.innerHTML = this.pages[page];
            } else {
                contentArea.innerHTML = `
                    <div class="alert alert-warning">
                        <h3>Page Not Found</h3>
                        <p>The requested page could not be loaded.</p>
                        <button class="btn btn-primary" onclick="app.navigateTo('dashboard')">Go to Dashboard</button>
                    </div>
                `;
            }

            // Update navigation state
            this.updateNavigation(page);
            this.updateBreadcrumb(this.getPageTitle(page));
            this.updatePageTitle(this.getPageTitle(page));
            this.currentPage = page;

            // Update browser history
            if (updateHistory) {
                const url = new URL(window.location);
                url.searchParams.set('page', page);
                history.pushState({ page }, '', url);
            }

            // Initialize page-specific functionality
            this.initializePageFeatures(page);
        }, 300);
    }

    updateNavigation(activePage) {
        // Remove active class from all nav links
        document.querySelectorAll('.sidebar-nav a').forEach(link => {
            link.classList.remove('active');
        });

        // Add active class to current page link
        const activeLink = document.querySelector(`[data-page="${activePage}"]`);
        if (activeLink) {
            activeLink.classList.add('active');
        }
    }

    updateBreadcrumb(pageTitle) {
        const breadcrumb = document.querySelector('.breadcrumb');
        const breadcrumbSpan = document.querySelector('#currentPageBreadcrumb');
        if (breadcrumb) {
            breadcrumb.innerHTML = `Super Admin / <span id="currentPageBreadcrumb">${pageTitle}</span>`;
        }
        if (breadcrumbSpan) {
            breadcrumbSpan.textContent = pageTitle;
        }
    }

    updatePageTitle(pageTitle) {
        const pageTitleElement = document.querySelector('#currentPageTitle');
        if (pageTitleElement) {
            pageTitleElement.textContent = pageTitle;
        }
    }

    getPageTitle(page) {
        const titles = {
            dashboard: 'Dashboard',
            manage_admins: 'Manage Admins',
            oversee_tenders: 'Oversee Tenders',
            reports: 'Reports',
            policies: 'Policies',
            notifications: 'Notifications'
        };
        return titles[page] || 'Unknown Page';
    }

    toggleMobileSidebar() {
        const sidebar = document.querySelector('.sidebar');
        const overlay = document.querySelector('.mobile-overlay');
        
        if (sidebar && overlay) {
            sidebar.classList.toggle('open');
            overlay.classList.toggle('show');
        }
    }

    closeMobileSidebar() {
        const sidebar = document.querySelector('.sidebar');
        const overlay = document.querySelector('.mobile-overlay');
        
        if (sidebar && overlay) {
            sidebar.classList.remove('open');
            overlay.classList.remove('show');
        }
    }

    logout() {
        if (confirm('Are you sure you want to logout?')) {
            // Clear any stored session data
            localStorage.removeItem('uvms_session');
            sessionStorage.clear();
            
            // Redirect to login page
            window.location.href = '../login.html';
        }
    }

    initializePageFeatures(page) {
        // Initialize page-specific features based on the current page
        switch (page) {
            case 'dashboard':
                this.initializeDashboard();
                break;
            case 'manage_admins':
                this.initializeManageAdmins();
                break;
            case 'oversee_tenders':
                this.initializeOverseetenders();
                break;
            case 'reports':
                this.initializeReports();
                break;
            case 'policies':
                this.initializePolicies();
                break;
            case 'notifications':
                this.initializeNotifications();
                break;
        }
    }

    initializeDashboard() {
        // Auto-refresh dashboard stats
        this.refreshDashboardStats();
        
        // Set up auto-refresh interval
        if (this.dashboardInterval) {
            clearInterval(this.dashboardInterval);
        }
        this.dashboardInterval = setInterval(() => {
            this.refreshDashboardStats();
        }, 30000); // Refresh every 30 seconds
    }

    initializeManageAdmins() {
        // Initialize admin management features
        console.log('Initializing Manage Admins page');
    }

    initializeOverseetenders() {
        // Initialize tender oversight features
        console.log('Initializing Oversee Tenders page');
    }

    initializeReports() {
        // Initialize reports features
        console.log('Initializing Reports page');
    }

    initializePolicies() {
        // Initialize policies features
        console.log('Initializing Policies page');
    }

    initializeNotifications() {
        // Initialize notifications features
        console.log('Initializing Notifications page');
    }

    refreshDashboardStats() {
        // Simulate real-time stats updates
        const stats = {
            totalColleges: Math.floor(Math.random() * 5) + 10,
            totalAdmins: Math.floor(Math.random() * 10) + 40,
            activeTenders: Math.floor(Math.random() * 10) + 25,
            totalVendors: Math.floor(Math.random() * 20) + 150
        };

        Object.keys(stats).forEach(key => {
            const element = document.getElementById(key);
            if (element) {
                element.textContent = stats[key];
            }
        });
    }

    // Utility methods
    showAlert(message, type = 'info') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        
        const contentArea = document.querySelector('.content-area');
        if (contentArea) {
            contentArea.insertBefore(alertDiv, contentArea.firstChild);
            
            setTimeout(() => {
                alertDiv.remove();
            }, 3000);
        }
    }

    // API simulation methods
    async simulateApiCall(endpoint, data = null) {
        // Simulate API delay
        await new Promise(resolve => setTimeout(resolve, 500 + Math.random() * 1000));
        
        // Simulate success/error responses
        if (Math.random() > 0.1) { // 90% success rate
            return { success: true, data: data || {} };
        } else {
            throw new Error('API call failed');
        }
    }
}

// Global utility functions
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('show');
    }
}

function hideModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('show');
    }
}

// Initialize the application when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.app = new SuperAdminApp();
});

// Export for module usage if needed
if (typeof module !== 'undefined' && module.exports) {
    module.exports = SuperAdminApp;
}

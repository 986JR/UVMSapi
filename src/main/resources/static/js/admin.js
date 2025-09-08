// UVMS Admin - Clean version without dark mode
document.addEventListener('DOMContentLoaded', function() {
    // Toggle sidebar
    const menuToggle = document.querySelector('.menu-toggle');
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');
    
    if (menuToggle && sidebar && mainContent) {
        menuToggle.addEventListener('click', function() {
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');
        });
    }

    // Logout functionality
    const logoutBtn = document.querySelector('.logout-btn');
    
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            if (confirm('Are you sure you want to logout?')) {
                window.location.href = 'login.html';
            }
        });
    }

    // Modal functionality
    const modals = document.querySelectorAll('.modal');
    
    modals.forEach(modal => {
        const closeBtn = modal.querySelector('.close-modal');
        const openBtn = document.getElementById(modal.id.replace('-modal', '-btn'));
        
        if (openBtn) {
            openBtn.addEventListener('click', function() {
                modal.style.display = 'flex';
            });
        }
        
        if (closeBtn) {
            closeBtn.addEventListener('click', function() {
                modal.style.display = 'none';
            });
        }
        
        window.addEventListener('click', function(e) {
            if (e.target === modal) {
                modal.style.display = 'none';
            }
        });
    });

    // Form submission handling
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            alert('Form submitted successfully!');
            
            const modal = this.closest('.modal');
            if (modal) {
                modal.style.display = 'none';
            }
            
            this.reset();
        });
    });

    // Responsive sidebar for mobile
    function handleMobileView() {
        if (window.innerWidth <= 768) {
            if (sidebar) sidebar.classList.add('collapsed');
            if (mainContent) mainContent.classList.add('expanded');
        } else {
            if (sidebar) sidebar.classList.remove('collapsed');
            if (mainContent) mainContent.classList.remove('expanded');
        }
    }
    
    window.addEventListener('resize', handleMobileView);
    handleMobileView();

    // Set active page for sidebar
    setActivePage();
});

function setActivePage() {
    const currentPage = window.location.pathname.split('/').pop();
    const navLinks = document.querySelectorAll('.sidebar-menu a');
    
    // Remove all active classes first
    navLinks.forEach(link => {
        link.classList.remove('active');
    });
    
    // Add active class to current page
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPage) {
            link.classList.add('active');
            
            setTimeout(() => {
                if (!isElementInViewport(link)) {
                    link.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }, 100);
        }
    });
}

function isElementInViewport(el) {
    const rect = el.getBoundingClientRect();
    const sidebarContainer = document.querySelector('.sidebar-scroll-container');
    const containerRect = sidebarContainer.getBoundingClientRect();
    
    return (
        rect.top >= containerRect.top &&
        rect.bottom <= containerRect.bottom
    );
}

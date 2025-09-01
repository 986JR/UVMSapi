// UVMS Main JavaScript Functions

// Snackbar/Toast Notification System
class UVMSSnackbar {
    constructor() {
        this.container = null;
        this.init();
    }

    init() {
        // Create snackbar container if it doesn't exist
        if (!document.querySelector('.snackbar-container')) {
            this.container = document.createElement('div');
            this.container.className = 'snackbar-container';
            document.body.appendChild(this.container);
        }
    }

    show(message, type = 'info', duration = 4000) {
        const snackbar = document.createElement('div');
        snackbar.className = `snackbar ${type}`;
        
        const icon = this.getIcon(type);
        
        snackbar.innerHTML = `
            <i class="snackbar-icon ${icon}"></i>
            <span class="snackbar-message">${message}</span>
            <button class="snackbar-close" onclick="this.parentElement.remove()">
                <i class="fas fa-times"></i>
            </button>
        `;

        document.body.appendChild(snackbar);

        // Trigger animation
        setTimeout(() => snackbar.classList.add('show'), 100);

        // Auto remove after duration
        if (duration > 0) {
            setTimeout(() => {
                snackbar.classList.remove('show');
                setTimeout(() => snackbar.remove(), 300);
            }, duration);
        }

        return snackbar;
    }

    getIcon(type) {
        const icons = {
            success: 'fas fa-check-circle',
            error: 'fas fa-exclamation-circle',
            warning: 'fas fa-exclamation-triangle',
            info: 'fas fa-info-circle'
        };
        return icons[type] || icons.info;
    }
}

// Initialize snackbar system
const uvmsSnackbar = new UVMSSnackbar();

// Global functions for easy access
window.showSnackbar = (message, type, duration) => uvmsSnackbar.show(message, type, duration);
window.showSuccess = (message, duration) => uvmsSnackbar.show(message, 'success', duration);
window.showError = (message, duration) => uvmsSnackbar.show(message, 'error', duration);
window.showWarning = (message, duration) => uvmsSnackbar.show(message, 'warning', duration);
window.showInfo = (message, duration) => uvmsSnackbar.show(message, 'info', duration);

// Mobile Menu Toggle
function toggleMobileMenu() {
    const sidebar = document.querySelector('.sidebar');
    const overlay = document.querySelector('.mobile-overlay');
    
    if (sidebar) {
        sidebar.classList.toggle('mobile-open');
        
        if (!overlay) {
            const mobileOverlay = document.createElement('div');
            mobileOverlay.className = 'mobile-overlay';
            mobileOverlay.onclick = closeMobileMenu;
            document.body.appendChild(mobileOverlay);
        }
        
        document.body.classList.toggle('mobile-menu-open');
    }
}

function closeMobileMenu() {
    const sidebar = document.querySelector('.sidebar');
    const overlay = document.querySelector('.mobile-overlay');
    
    if (sidebar) {
        sidebar.classList.remove('mobile-open');
    }
    
    if (overlay) {
        overlay.remove();
    }
    
    document.body.classList.remove('mobile-menu-open');
}

// Form Validation Helpers
function validateRequired(input) {
    const value = input.value.trim();
    const isValid = value !== '';
    
    if (isValid) {
        input.classList.remove('error');
    } else {
        input.classList.add('error');
    }
    
    return isValid;
}

function validateEmail(input) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isValid = emailRegex.test(input.value.trim());
    
    if (isValid) {
        input.classList.remove('error');
    } else {
        input.classList.add('error');
    }
    
    return isValid;
}

function validatePhone(input) {
    const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
    const isValid = phoneRegex.test(input.value.replace(/\s/g, ''));
    
    if (isValid) {
        input.classList.remove('error');
    } else {
        input.classList.add('error');
    }
    
    return isValid;
}

// Initialize on DOM load
document.addEventListener('DOMContentLoaded', function() {
    // Connect existing mobile menu toggle button
    const existingToggle = document.querySelector('.mobile-menu-toggle');
    if (existingToggle) {
        existingToggle.onclick = toggleMobileMenu;
    }
    
    // Close mobile menu when clicking on nav links
    document.querySelectorAll('.sidebar-nav a').forEach(link => {
        link.addEventListener('click', closeMobileMenu);
    });
    
    // Handle window resize
    window.addEventListener('resize', function() {
        if (window.innerWidth > 768) {
            closeMobileMenu();
        }
    });
    
    // Fix viewport issues on mobile
    const viewport = document.querySelector('meta[name="viewport"]');
    if (viewport) {
        viewport.setAttribute('content', 'width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no');
    }
    
    // Add touch event handling for better mobile experience
    let touchStartX = 0;
    let touchStartY = 0;
    
    document.addEventListener('touchstart', function(e) {
        touchStartX = e.touches[0].clientX;
        touchStartY = e.touches[0].clientY;
    });
    
    document.addEventListener('touchmove', function(e) {
        if (!touchStartX || !touchStartY) return;
        
        const touchEndX = e.touches[0].clientX;
        const touchEndY = e.touches[0].clientY;
        
        const diffX = touchStartX - touchEndX;
        const diffY = touchStartY - touchEndY;
        
        // Swipe right to open menu
        if (Math.abs(diffX) > Math.abs(diffY) && diffX < -50 && touchStartX < 50) {
            if (window.innerWidth <= 768) {
                toggleMobileMenu();
            }
        }
        
        // Swipe left to close menu
        if (Math.abs(diffX) > Math.abs(diffY) && diffX > 50) {
            if (window.innerWidth <= 768) {
                closeMobileMenu();
            }
        }
    });
});
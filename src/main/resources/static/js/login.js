/**
 * login.js
 * Interactions du formulaire de connexion
 */

document.addEventListener('DOMContentLoaded', function() {
    console.log('Login page loaded');

    // Animation des rôles
    animateRoles();
});

/**
 * Ajouter une animation aux rôles
 */
function animateRoles() {
    const roles = document.querySelectorAll('.role-item');

    roles.forEach((role, index) => {
        // Animation d'entrée
        role.style.opacity = '0';
        role.style.transform = 'translateY(10px)';

        setTimeout(() => {
            role.style.transition = 'all 0.3s ease';
            role.style.opacity = '1';
            role.style.transform = 'translateY(0)';
        }, index * 100);
    });
}

// Export
window.Login = {
    animateRoles: animateRoles
};
/**
 * dashboard-chef.js
 * Interactions et améliorations du dashboard Chef de Département
 */

document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard Chef chargé');

    // Initialiser les interactions
    initializeButtonEffects();
    initializeTableRows();
});

/**
 * Ajouter des effets aux boutons
 */
function initializeButtonEffects() {
    const buttons = document.querySelectorAll('.btn');

    buttons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-1px)';
        });

        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
}

/**
 * Améliorer l'interaction avec les lignes du tableau
 */
function initializeTableRows() {
    const rows = document.querySelectorAll('tbody tr');

    rows.forEach(row => {
        row.addEventListener('click', function(e) {
            // Ignorer si on clique sur un lien ou un bouton
            if (e.target.tagName !== 'A' && e.target.tagName !== 'BUTTON') {
                this.style.opacity = '0.8';
                setTimeout(() => {
                    this.style.opacity = '1';
                }, 150);
            }
        });
    });
}

/**
 * Fonction optionnelle : Ajouter des confirmations aux formulaires sensibles
 */
function confirmAction(message = 'Êtes-vous sûr ?') {
    return confirm(message);
}

// Export pour utilisation si nécessaire
window.DashboardChef = {
    confirmAction: confirmAction
};
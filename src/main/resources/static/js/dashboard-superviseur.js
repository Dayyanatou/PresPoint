/**
 * dashboard-superviseur.js
 * Interactions et améliorations du dashboard superviseur
 */

document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard Superviseur chargé');

    // Initialiser les effets des boutons
    initializeButtonEffects();

    // Initialiser les animations des lignes
    initializeRowInteractions();
});

/**
 * Ajouter des effets de survol et de clic aux boutons
 */
function initializeButtonEffects() {
    const buttons = document.querySelectorAll('.btn');

    buttons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });

        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
}

/**
 * Améliorer l'interaction avec les demandes
 */
function initializeRowInteractions() {
    const items = document.querySelectorAll('.pointage-item');

    items.forEach(item => {
        item.style.cursor = 'pointer';

        item.addEventListener('click', function(e) {
            // Ignorer si on clique sur un lien ou un bouton
            if (e.target.closest('button, form')) {
                return;
            }

            // Petit effet de pulsation
            this.style.opacity = '0.85';
            setTimeout(() => {
                this.style.opacity = '1';
            }, 150);
        });
    });
}

// Export pour utilisation si nécessaire
window.DashboardSuperviseur = {
    initializeButtonEffects: initializeButtonEffects,
    initializeRowInteractions: initializeRowInteractions
};
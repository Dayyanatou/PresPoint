/**
 * dashboard-etudiant.js
 * Interactions et améliorations du dashboard étudiant
 */

document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard Étudiant chargé');

    // Initialiser les effets des boutons
    initializeButtonEffects();

    // Initialiser les interactions des lignes
    initializeRowInteractions();

    // Initialiser les animations des barres de progression
    initializeProgressBars();
});

/**
 * Ajouter des effets de survol et de clic aux boutons
 */
function initializeButtonEffects() {
    const buttons = document.querySelectorAll('.btn');

    buttons.forEach(button => {
        // Effet au survol
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });

        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });

        // Effet au clic
        button.addEventListener('click', function(e) {
            // Créer un effet de ripple
            const ripple = document.createElement('span');
            ripple.style.position = 'absolute';
            ripple.style.borderRadius = '50%';
            ripple.style.background = 'rgba(255,255,255,0.5)';
            ripple.style.pointerEvents = 'none';

            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;

            ripple.style.width = size + 'px';
            ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.style.animation = 'ripple 0.6s ease-out';

            this.style.position = 'relative';
            this.style.overflow = 'hidden';
            this.appendChild(ripple);

            setTimeout(() => ripple.remove(), 600);
        });
    });

    // Ajouter l'animation du ripple au CSS
    if (!document.querySelector('style[data-ripple]')) {
        const style = document.createElement('style');
        style.setAttribute('data-ripple', 'true');
        style.textContent = `
            @keyframes ripple {
                from {
                    opacity: 1;
                    transform: scale(0);
                }
                to {
                    opacity: 0;
                    transform: scale(1);
                }
            }
        `;
        document.head.appendChild(style);
    }
}

/**
 * Améliorer l'interaction avec les lignes de tableaux
 */
function initializeRowInteractions() {
    const rows = document.querySelectorAll('tbody tr, .seance-item, .progress-row');

    rows.forEach(row => {
        row.style.cursor = 'pointer';

        row.addEventListener('click', function(e) {
            // Ignorer si on clique sur un lien ou un bouton
            if (e.target.closest('a, button, form')) {
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

/**
 * Animer les barres de progression lors du chargement
 */
function initializeProgressBars() {
    const progressFills = document.querySelectorAll('.prog-fill');

    progressFills.forEach(fill => {
        const width = fill.style.width;
        fill.style.width = '0';
        fill.style.transition = 'width 1.2s cubic-bezier(0.25, 0.46, 0.45, 0.94)';

        // Déclencher l'animation après un court délai
        setTimeout(() => {
            fill.style.width = width;
        }, 100);
    });
}

// Export pour utilisation si nécessaire
window.DashboardEtudiant = {
    initializeButtonEffects: initializeButtonEffects,
    initializeRowInteractions: initializeRowInteractions,
    initializeProgressBars: initializeProgressBars
};
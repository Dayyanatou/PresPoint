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


let modalAccepter = true; // true = accepter, false = refuser
    let justificatifIdCourant = null;

    // Ouvre le modal avec les bonnes infos selon le bouton cliqué
    function ouvrirModal(justificatifId, accepter) {
      justificatifIdCourant = justificatifId;
      modalAccepter = accepter;

      // Changer le titre selon l'action
      document.getElementById('modal-titre').textContent =
        accepter ? 'Accepter ce justificatif' : 'Refuser ce justificatif';

      // Changer la couleur du bouton confirmer
      const btnConfirmer = document.getElementById('modal-btn-confirmer');
      btnConfirmer.style.background = accepter ? '#0F6E56' : '#c0392b';

      // Afficher le modal
      document.getElementById('modal-commentaire').classList.add('visible');
      document.getElementById('modal-commentaire-texte').value = '';
    }

    function fermerModal() {
      document.getElementById('modal-commentaire').classList.remove('visible');
    }

    // Soumet le formulaire vers la bonne URL (accepter ou refuser)
    function confirmerAction() {
      const commentaire = document.getElementById('modal-commentaire-texte').value;
      document.getElementById('modal-commentaire-hidden').value = commentaire;

      // Construire l'URL : /chef/justificatifs/{id}/accepter ou /refuser
      const action = modalAccepter ? 'accepter' : 'refuser';
      const form = document.getElementById('modal-form');
      form.action = '/chef/justificatifs/' + justificatifIdCourant + '/' + action;

      form.submit();
      fermerModal();
    }

    // Fermer le modal si on clique en dehors
    document.getElementById('modal-commentaire').addEventListener('click', function(e) {
      if (e.target === this) fermerModal();
    });
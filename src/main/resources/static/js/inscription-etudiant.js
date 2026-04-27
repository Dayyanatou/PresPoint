/**
 * inscription-etudiant.js
 * Validations du formulaire d'inscription
 */

document.addEventListener('DOMContentLoaded', function() {
    console.log('Inscription page loaded');

    const form = document.querySelector('.inscription-form');
    if (form) {
        form.addEventListener('submit', validateForm);
    }
});

/**
 * Valider le formulaire avant soumission
 */
function validateForm(e) {
    const motDePasse = document.querySelector('input[name="motDePasse"]').value;
    const confirmMotDePasse = document.querySelector('input[name="confirmMotDePasse"]').value;

    // Vérifier que les mots de passe correspondent
    if (motDePasse !== confirmMotDePasse) {
        e.preventDefault();
        alert('❌ Les mots de passe ne correspondent pas !');
        return false;
    }

    // Vérifier la longueur
    if (motDePasse.length < 8) {
        e.preventDefault();
        alert('❌ Le mot de passe doit contenir au moins 8 caractères !');
        return false;
    }

    return true;
}

window.Inscription = {
    validateForm: validateForm
};
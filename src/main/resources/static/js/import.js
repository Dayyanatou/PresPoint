// Affiche le nom du fichier sélectionné sous la zone d'upload
function afficherFichier(input) {
    const p = document.getElementById('nom-fichier');
    if (input.files.length > 0) {
        p.textContent = '✓ Fichier sélectionné : ' + input.files[0].name;
    }
}
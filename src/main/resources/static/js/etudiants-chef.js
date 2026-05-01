// Filtre le tableau selon la recherche texte côté client
function filtrerTableau() {
    const recherche = document.getElementById('recherche').value.toLowerCase();
    const lignes = document.querySelectorAll('#tableau-etudiants tbody tr');
    lignes.forEach(ligne => {
        const texte = ligne.textContent.toLowerCase();
        ligne.style.display = texte.includes(recherche) ? '' : 'none';
    });
}
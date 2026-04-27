document.addEventListener("DOMContentLoaded", function () {

    const roleSelect = document.getElementById("roleSelect");

    const etudiantFields = document.getElementById("etudiantFields");
    const enseignantFields = document.getElementById("enseignantFields");

    function updateForm() {
        const role = roleSelect.value;

        // cacher tout
        etudiantFields.style.display = "none";
        enseignantFields.style.display = "none";

        // afficher selon rôle
        if (role === "ETUDIANT") {
            etudiantFields.style.display = "block";
        }

        if (role === "ENSEIGNANT") {
            enseignantFields.style.display = "block";
        }
    }

    roleSelect.addEventListener("change", updateForm);

    // initialisation au chargement
    updateForm();
});
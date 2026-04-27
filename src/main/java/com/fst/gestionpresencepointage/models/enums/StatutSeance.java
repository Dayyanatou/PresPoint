package com.fst.gestionpresencepointage.models.enums;

public enum StatutSeance {
    PLANIFIEE, //La séance est prévue, personne n'a encore pointé.
    EN_COURS, //L'enseignant a cliqué sur "Démarrer".
    REALISEE,//L'enseignant a cliqué sur "Terminer".
    ANNULEE
}

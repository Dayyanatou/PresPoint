package com.fst.gestionpresencepointage.models.enums;

public enum StatutJustificatif {
    EN_ATTENTE,  // soumis, pas encore traité
    ACCEPTE,     // chef a validé → absence devient JUSTIFIE
    REFUSE       // chef a refusé
}
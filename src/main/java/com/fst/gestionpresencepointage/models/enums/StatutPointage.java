package com.fst.gestionpresencepointage.models.enums;

public enum StatutPointage {
    EN_ATTENTE,//Le pointage est fini, il attend d'être vu.
    CONFIRME, // Le superviseur a confirmé la présence physique de l'enseignant.
    VALIDE, //Le Chef de département a donné son accord final (pour le paiement).
    REJETE // quand Il y a eu un problème.
}

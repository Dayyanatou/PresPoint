package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.JustificatifDto;
import com.fst.gestionpresencepointage.models.enums.StatutJustificatif;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface JustificatifService {

    // Étudiant soumet un justificatif avec fichier
    // MultipartFile = le fichier uploadé par le formulaire HTML
    JustificatifDto soumettre(Long etudiantId,
                              Long presenceId,
                              String motif,
                              String description,
                              MultipartFile fichier);

    // Chef accepte → la présence passe à JUSTIFIE automatiquement
    JustificatifDto accepter(Long justificatifId,
                             Long chefId,
                             String commentaire);

    // Chef refuse
    JustificatifDto refuser(Long justificatifId,
                            Long chefId,
                            String commentaire);

    // Liste des justificatifs d'un étudiant
    List<JustificatifDto> listerParEtudiant(Long etudiantId);

    // Liste des justificatifs EN_ATTENTE pour le chef
    List<JustificatifDto> listerEnAttenteParDepartement(Long departementId);
}
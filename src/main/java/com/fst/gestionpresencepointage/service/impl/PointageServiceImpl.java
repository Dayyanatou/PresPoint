package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.PointageDto;
import com.fst.gestionpresencepointage.models.*;
import com.fst.gestionpresencepointage.models.enums.StatutPointage;
import com.fst.gestionpresencepointage.repository.*;
import com.fst.gestionpresencepointage.service.PointageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PointageServiceImpl implements PointageService {

    private final PointageRepository pointageRepository;
    private final EnseignantRepository enseignantRepository;
    private final SeanceRepository seanceRepository;
    private final SuperviseurRepository superviseurRepository;
    private final ChefDepartementRepository chefDepartementRepository;

    @Override
    public PointageDto soumettre(PointageDto dto) {
        // Règle : un pointage par séance maximum
        Seance seance = seanceRepository.findById(dto.getSeanceId())
                .orElseThrow(() -> new RuntimeException("Séance introuvable."));
        if (pointageRepository.existsBySeance(seance)) {
            throw new RuntimeException("Un pointage existe déjà pour cette séance.");
        }

        Enseignant enseignant = enseignantRepository.findById(dto.getEnseignantId())
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable."));

        Pointage pointage = new Pointage();
        pointage.setEnseignant(enseignant);
        pointage.setSeance(seance);
        pointage.setHeureDebut(dto.getHeureDebut());
        pointage.setHeureFin(dto.getHeureFin());
        pointage.setDate(dto.getDate());
        //pointage.setRemarque(dto.getRemarque());
        pointage.setStatut(StatutPointage.EN_ATTENTE); // toujours EN_ATTENTE à la soumission

        return PointageDto.fromEntity(pointageRepository.save(pointage));
    }

    @Override
    public PointageDto confirmer(Long id, Long superviseurId) {
        // Superviseur confirme le pointage
        Pointage pointage = pointageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pointage introuvable."));
        Superviseur superviseur = superviseurRepository.findById(superviseurId)
                .orElseThrow(() -> new RuntimeException("Superviseur introuvable."));

        pointage.setSuperviseur(superviseur); // on enregistre qui a confirmé
        // statut reste EN_ATTENTE — attend encore la validation du chef
        return PointageDto.fromEntity(pointageRepository.save(pointage));
    }

    @Override
    public PointageDto valider(Long id, Long chefId) {

        // Chef de département valide définitivement
        Pointage pointage = pointageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pointage introuvable."));
        ChefDepartement chef = chefDepartementRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef de département introuvable."));

        pointage.setChefDepartement(chef);
        pointage.setStatut(StatutPointage.VALIDE); // ✅ validation finale
        return PointageDto.fromEntity(pointageRepository.save(pointage));
    }

    @Override
    public PointageDto rejeter(Long id, Long chefId) {
        Pointage pointage = pointageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pointage introuvable."));
        ChefDepartement chef = chefDepartementRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef de département introuvable."));

        pointage.setChefDepartement(chef);
        pointage.setStatut(StatutPointage.REJETE); // ❌ rejeté — enseignant doit corriger
        return PointageDto.fromEntity(pointageRepository.save(pointage));
    }

    @Override
    public PointageDto getById(Long id) {
        return PointageDto.fromEntity(
                pointageRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Pointage introuvable."))
        );
    }

    @Override
    public List<PointageDto> listerParEnseignant(Long enseignantId) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable."));
        return pointageRepository.findByEnseignant(enseignant).stream()
                .map(PointageDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<PointageDto> listerParStatut(StatutPointage statut) {
        return pointageRepository.findByStatut(statut).stream()
                .map(PointageDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<PointageDto> listerEnAttente() {
        return listerParStatut(StatutPointage.EN_ATTENTE);
    }
    // À ajouter à la fin de PointageServiceImpl.java
    @Override
    public List<PointageDto> listerEnAttenteParDepartement(Long departementId) {
        return pointageRepository
                .findByStatutAndDepartement(StatutPointage.EN_ATTENTE, departementId)
                .stream()
                .map(PointageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
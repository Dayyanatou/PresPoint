package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.PointageDto;
import com.fst.gestionpresencepointage.repository.SuperviseurRepository;
import com.fst.gestionpresencepointage.service.PointageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/superviseur")
@RequiredArgsConstructor
public class DashboardSuperviseurController {

    private final SuperviseurRepository superviseurRepository;
    private final PointageService pointageService;

    // Dashboard principal
    @GetMapping("/dashboard/{superviseurId}")
    public String dashboard(@PathVariable Long superviseurId, Model model) {
        var superviseur = superviseurRepository.findById(superviseurId)
                .orElseThrow(() -> new RuntimeException("Superviseur introuvable"));

        List<PointageDto> pointagesEnAttente = pointageService.listerEnAttente();

        model.addAttribute("superviseur", superviseur);
        model.addAttribute("pointagesEnAttente", pointagesEnAttente);
        model.addAttribute("nbEnAttente", pointagesEnAttente.size());
        return "superviseur/dashboard";
    }

    // Confirmer un pointage
    @PostMapping("/pointage/{pointageId}/confirmer/{superviseurId}")
    public String confirmer(@PathVariable Long pointageId,
                            @PathVariable Long superviseurId) {
        pointageService.confirmer(pointageId, superviseurId);
        return "redirect:/superviseur/dashboard/" + superviseurId;
    }

    // Rejeter un pointage
    @PostMapping("/pointage/{pointageId}/rejeter/{superviseurId}")
    public String rejeter(@PathVariable Long pointageId,
                          @PathVariable Long superviseurId) {
        pointageService.rejeter(pointageId, superviseurId);
        return "redirect:/superviseur/dashboard/" + superviseurId;
    }
}
package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.DepartementDto;
import com.fst.gestionpresencepointage.service.DepartementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/departements")
@RequiredArgsConstructor
public class DepartementController {

    private final DepartementService departementService;

    // ✅ LISTE
    @GetMapping
    public String liste(Model model) {
        List<DepartementDto> departements = departementService.listerTous();
        model.addAttribute("departements", departements);
        return "departements/liste";
    }

    // ✅ FORM AJOUT
    @GetMapping("/new")
    public String formAjout(Model model) {
        model.addAttribute("departement", new DepartementDto());
        return "departements/form";
    }

    // ✅ SAVE
    @PostMapping
    public String save(@ModelAttribute("departement") DepartementDto dto) {
        departementService.enregistrer(dto);
        return "redirect:/departements";
    }

    // ✅ EDIT
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        DepartementDto dto = departementService.rechercherParId(id);
        model.addAttribute("departement", dto);
        return "departements/form";
    }

    // ✅ DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        departementService.supprimer(id);
        return "redirect:/departements";
    }
}
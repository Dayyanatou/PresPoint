package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.EnseignantDto;
import com.fst.gestionpresencepointage.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enseignants")
@RequiredArgsConstructor
public class EnseignantWebController {

    private final EnseignantService enseignantService;

    // ✅ LISTE
    @GetMapping
    public String liste(Model model) {
        List<EnseignantDto> enseignants = enseignantService.listerTous();
        model.addAttribute("enseignants", enseignants);
        return "enseignants/liste";
    }

    // ✅ FORM AJOUT
    @GetMapping("/new")
    public String formAjout(Model model) {
        model.addAttribute("enseignant", new EnseignantDto());
        return "enseignants/form";
    }

    // ✅ SAVE
    @PostMapping
    public String save(@ModelAttribute("enseignant") EnseignantDto dto) {
        enseignantService.enregistrer(dto);
        return "redirect:/enseignants";
    }

    // ✅ EDIT
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        EnseignantDto dto = enseignantService.rechercherParId(id);
        model.addAttribute("enseignant", dto);
        return "enseignants/form";
    }

    // ✅ DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        enseignantService.supprimer(id);
        return "redirect:/enseignants";
    }
}
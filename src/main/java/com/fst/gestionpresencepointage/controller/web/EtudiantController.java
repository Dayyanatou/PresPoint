package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import com.fst.gestionpresencepointage.service.DepartementService;
import com.fst.gestionpresencepointage.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final DepartementService departementService;

    // ====================== LISTE ======================
    @GetMapping
    public String liste(Model model) {
        model.addAttribute("etudiants", etudiantService.listerTous());
        return "etudiants/liste";
    }

    // ====================== FORMULAIRE AJOUT ======================
    @GetMapping("/new")
    public String formAjout(Model model) {
        model.addAttribute("etudiant", new EtudiantDto());
        model.addAttribute("departements", departementService.listerTous());
        return "etudiants/form";
    }

    // ====================== FORMULAIRE MODIFICATION ======================
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        model.addAttribute("etudiant", etudiantService.rechercherParId(id));
        model.addAttribute("departements", departementService.listerTous());
        return "etudiants/form";
    }

    // ====================== SAVE (CREATE + UPDATE) ======================
    @PostMapping
    public String save(@ModelAttribute EtudiantDto dto, Model model) {
        try {
            if (dto.getId() != null && dto.getId() > 0) {
                // Mode Modification
                etudiantService.modifier(dto.getId(), dto);
            } else {
                // Mode Création
                etudiantService.enregistrer(dto);
            }
            return "redirect:/etudiants";
        }
        catch (RuntimeException e) {
            // En cas d'erreur, on reste sur le formulaire
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("etudiant", dto);
            model.addAttribute("departements", departementService.listerTous());
            return "etudiants/form";
        }
    }

    // ====================== SUPPRESSION ======================
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        etudiantService.supprimer(id);
        return "redirect:/etudiants";
    }
}


/*
package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import com.fst.gestionpresencepointage.service.DepartementService;
import com.fst.gestionpresencepointage.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/etudiants")
@RequiredArgsConstructor
public class EtudiantRestController {

    private final EtudiantService etudiantService;
    private final DepartementService departementService;

    // 📌 LISTE
    @GetMapping
    public String liste(Model model) {
        model.addAttribute("etudiants", etudiantService.listerTous());
        return "etudiants/liste";
    }

    // 📌 FORM AJOUT
    @GetMapping("/new")
    public String formAjout(Model model) {
        model.addAttribute("etudiant", new EtudiantDto());
        // 🔥 AJOUT IMPORTANT
        model.addAttribute("departements", departementService.listerTous());

        return "etudiants/form";
    }

    // 📌 FORM EDIT
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        model.addAttribute("etudiant", etudiantService.rechercherParId(id));

        // 🔥 SINON la liste sera vide !
        model.addAttribute("departements", departementService.listerTous());
        return "etudiants/form";
    }

    // 📌 SAVE (CREATE + UPDATE corrigé)
    @PostMapping
    public String save(@ModelAttribute EtudiantDto dto, Model model) {

        try {
            if (dto.getId() != null) {
                etudiantService.modifier(dto.getId(), dto);
            } else {
                etudiantService.enregistrer(dto);
            }

            return "redirect:/etudiants";

        } catch (RuntimeException e) {
            // On renvoie vers le formulaire avec le message d'erreur
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("etudiant", dto);           // pour garder les données saisies
            model.addAttribute("departements", departementService.listerTous());

            return "etudiants/form";   // on reste sur le formulaire
        }
    }

    // 📌 DELETE (corrigé en POST)
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        etudiantService.supprimer(id);
        return "redirect:/etudiants";
    }
}

*/

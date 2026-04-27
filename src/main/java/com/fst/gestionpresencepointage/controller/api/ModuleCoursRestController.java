package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.models.ModuleCours;
import com.fst.gestionpresencepointage.repository.ModuleCoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/modules-cours")
@CrossOrigin("*")
public class ModuleCoursRestController {

    @Autowired
    private ModuleCoursRepository moduleCoursRepository;

    @PostMapping
    public ModuleCours save(@RequestBody ModuleCours moduleCours) {
        return moduleCoursRepository.save(moduleCours);
    }

    @GetMapping
    public List<ModuleCours> getAll() {
        return moduleCoursRepository.findAll();
    }
}

package kfu.itis.controller;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.service.SpecializationService;
import kfu.itis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/masters")
public class MasterController {

    private final UserService userService;
    private final SpecializationService specializationService;

    public MasterController(UserService userService, SpecializationService specializationService) {
        this.userService = userService;
        this.specializationService = specializationService;
    }

    @GetMapping
    public String masters(@RequestParam(required = false) Long specializationId,
                          @RequestParam(required = false) Double minRating,
                          @RequestParam(required = false) String query,
                          Model model) {

        List<User> masters;

        if (query != null && !query.isEmpty()) {
            masters = userService.findMastersByName(query);
        } else if (specializationId != null && minRating != null) {
            Specialization specialization = specializationService.findById(specializationId).orElseThrow();
            masters = userService.findMastersBySpecializationAndMinRating(specialization, minRating);
        } else if (specializationId != null) {
            Specialization specialization = specializationService.findById(specializationId).orElseThrow();
            masters = userService.findAllMastersBySpecialization(specialization);
        } else if (minRating != null) {
            masters = userService.findMastersByMinRating(minRating);
        } else {
            masters = userService.findAllMasters();
        }

        model.addAttribute("masters", masters);
        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("minRating", minRating);
        model.addAttribute("query", query);
        model.addAttribute("selectedSpecializationId", specializationId);

        return "masters/catalog";
    }

    @GetMapping("/{id}")
    public String masterDetails(@PathVariable Long id, Model model) {
        User master = userService.findByIdWithSpecializations(id)
                .orElseThrow(() -> new RuntimeException("user not found"));
        model.addAttribute("master", master);
        return "masters/details";
    }
}

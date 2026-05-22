package kfu.itis.controller;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import kfu.itis.service.FavoriteMasterService;
import kfu.itis.service.SpecializationService;
import kfu.itis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/masters")
public class MasterController {

    private final UserService userService;
    private final SpecializationService specializationService;
    private final FavoriteMasterService  favoriteMasterService;

    public MasterController(UserService userService, SpecializationService specializationService,  FavoriteMasterService favoriteMasterService) {
        this.userService = userService;
        this.specializationService = specializationService;
        this.favoriteMasterService = favoriteMasterService;
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
    public String masterDetails(@PathVariable Long id, Model model, Principal principal) {
        User master = userService.findByIdWithSpecializations(id)
                .orElseThrow(() -> new RuntimeException("user not found"));
        model.addAttribute("master", master);

        if (principal != null) {
            User customer = userService.findByUsername(principal.getName()).orElse(null);
            if (customer != null && customer.getRole() == Role.CUSTOMER) {
                boolean isFavorite = favoriteMasterService.isFavorite(customer, master);
                model.addAttribute("isFavorite", isFavorite);
            }
        }

        return "masters/details";
    }
}

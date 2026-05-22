package kfu.itis.controller;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import kfu.itis.service.SpecializationService;
import kfu.itis.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/master")
public class MasterProfileController {

    private final UserService userService;
    private final SpecializationService specializationService;

    public MasterProfileController(UserService userService, SpecializationService specializationService) {
        this.userService = userService;
        this.specializationService = specializationService;
    }

    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkSpecializations(Principal principal) {
        Map<String, Boolean> response = new HashMap<>();
        if (principal == null) {
            response.put("hasSpecializations", true);
            return ResponseEntity.ok(response);
        }
        User master = userService.findByUsername(principal.getName()).orElse(null);
        if (master == null || master.getRole() != Role.MASTER) {
            response.put("hasSpecializations", true);
            return ResponseEntity.ok(response);
        }
        boolean hasSpecializations = master.getSpecializations() != null && !master.getSpecializations().isEmpty();
        response.put("hasSpecializations", hasSpecializations);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/specializations")
    public String chooseSpecializations(Model model, Principal principal) {
        User master = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (master.getRole() != Role.MASTER) {
            return "redirect:/";
        }

        model.addAttribute("master", master);
        model.addAttribute("allSpecializations", specializationService.findAll());
        model.addAttribute("userSpecializations", master.getSpecializations());
        return "master/choose-specializations";
    }

    @PostMapping("/specializations")
    public String saveSpecializations(@RequestParam(required = false) Long[] specializationIds,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        User master = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (master.getRole() != Role.MASTER) {
            return "redirect:/";
        }

        if (specializationIds == null || specializationIds.length == 0) {
            redirectAttributes.addFlashAttribute("error", "Выберите хотя бы одну специализацию");
            return "redirect:/master/specializations";
        }

        Set<Specialization> specializations = new HashSet<>();
        for (Long id : specializationIds) {
            specializationService.findById(id).ifPresent(specializations::add);
        }
        master.setSpecializations(specializations);
        userService.save(master);

        redirectAttributes.addFlashAttribute("success", "Специализации успешно сохранены");
        return "redirect:/master/specializations";
    }
}
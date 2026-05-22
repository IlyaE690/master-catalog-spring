package kfu.itis.controller;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.enums.Role;
import kfu.itis.service.AuthService;
import kfu.itis.service.SpecializationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;
    private final SpecializationService specializationService;

    public AuthController(AuthService authService, SpecializationService specializationService) {
        this.authService = authService;
        this.specializationService = specializationService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String expired,
                        @RequestParam(required = false) String registered,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверный логин или пароль");
        }

        if (expired != null) {
            model.addAttribute("error", "Войдите снова");
        }

        if (registered != null) {
            model.addAttribute("registered", true);
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("roles", new Role[]{Role.CUSTOMER, Role.MASTER});
        model.addAttribute("specializations", specializationService.findAll());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(required = false) String firstName,
                           @RequestParam(required = false) String lastName,
                           @RequestParam(required = false) String phone,
                           @RequestParam Role role,
                           @RequestParam(required = false) Long[] specializations,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (role == Role.ADMIN) {
            model.addAttribute("error", "Некорректная роль");
            model.addAttribute("roles", new Role[]{Role.CUSTOMER, Role.MASTER});
            model.addAttribute("specializations", specializationService.findAll());
            return "auth/register";
        }

        if (role == Role.MASTER && (specializations == null || specializations.length == 0)) {
            model.addAttribute("error", "Для регистрации как мастер выберите хотя бы одну специализацию");
            model.addAttribute("roles", new Role[]{Role.CUSTOMER, Role.MASTER});
            model.addAttribute("specializations", specializationService.findAll());
            return "auth/register";
        }

        try {
            authService.register(username, email, password, firstName, lastName, phone, role, specializations);
            redirectAttributes.addAttribute("registered", true);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", new Role[]{Role.CUSTOMER, Role.MASTER});
            model.addAttribute("specializations", specializationService.findAll());
            return "auth/register";
        }
    }
}
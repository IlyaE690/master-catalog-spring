package kfu.itis.controller;

import kfu.itis.model.entity.User;
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
                           @RequestParam(required = false) Long[] specializationIds,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("phone", phone);
        model.addAttribute("role", role);

        if (role == Role.ADMIN) {
            model.addAttribute("error", "Некорректная роль");
            return "auth/register";
        }

        try {
            authService.register(username, email, password, firstName, lastName, phone, role, specializationIds);
            redirectAttributes.addAttribute("registered", true);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}

package kfu.itis.controller;

import kfu.itis.model.entity.FavoriteMaster;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import kfu.itis.service.FavoriteMasterService;
import kfu.itis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteMasterService favoriteMasterService;
    private final UserService userService;

    public FavoriteController(FavoriteMasterService favoriteMasterService, UserService userService) {
        this.favoriteMasterService = favoriteMasterService;
        this.userService = userService;
    }

    @GetMapping
    public String favorites(Principal principal, Model model) {
        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<FavoriteMaster> favorites = favoriteMasterService.findByCustomerWithDetails(customer);
        model.addAttribute("favorites", favorites);
        return "favorites/list";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long masterId, Principal principal, RedirectAttributes redirectAttributes) {
        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        User master = userService.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));

        if (master.getRole() != Role.MASTER) {
            redirectAttributes.addFlashAttribute("error", "Некорректный ID мастера");
            return "redirect:/masters/" + masterId;
        }

        if (favoriteMasterService.isFavorite(customer, master)) {
            redirectAttributes.addFlashAttribute("info", "Мастер уже в избранном");
        } else {
            favoriteMasterService.add(customer, master, null);
            redirectAttributes.addFlashAttribute("success", "Мастер добавлен в избранное");
        }

        return "redirect:/favorites";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long masterId, Principal principal, RedirectAttributes redirectAttributes) {
        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        User master = userService.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));

        favoriteMasterService.remove(customer, master);
        redirectAttributes.addFlashAttribute("success", "Мастер удалён из избранного");
        return "redirect:/favorites";
    }
}
package kfu.itis.controller;

import kfu.itis.model.entity.FavoriteMaster;
import kfu.itis.model.entity.User;
import kfu.itis.service.FavoriteMasterService;
import kfu.itis.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
        User customer = userService.findByUsername(principal.getName()).orElseThrow();

        List<FavoriteMaster> favorites = favoriteMasterService.findByCustomer(customer);
        model.addAttribute("favorites", favorites);
        return "favorites/list";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> add(@RequestParam Long masterId, Principal principal) {
        User customer = userService.findByUsername(principal.getName()).orElseThrow();
        User master = userService.findById(masterId).orElseThrow();

        if (favoriteMasterService.isFavorite(customer, master)) {
            return ResponseEntity.ok(Map.of("success", false, "message", "Уже в избранном"));
        }

        favoriteMasterService.add(customer, master, null);
        return ResponseEntity.ok(Map.of("success", true, "message", "Добавлено в избранное"));
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> remove(@RequestParam Long masterId, Principal principal) {
        User customer = userService.findByUsername(principal.getName()).orElseThrow();
        User master = userService.findById(masterId).orElseThrow();
        favoriteMasterService.remove(customer, master);
        return ResponseEntity.ok(Map.of("success", true, "message", "Удалено из избранного"));
    }
}


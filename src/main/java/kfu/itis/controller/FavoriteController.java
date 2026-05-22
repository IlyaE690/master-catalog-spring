package kfu.itis.controller;

import kfu.itis.model.entity.FavoriteMaster;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import kfu.itis.service.FavoriteMasterService;
import kfu.itis.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
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
        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<FavoriteMaster> favorites = favoriteMasterService.findByCustomerWithDetails(customer);
        model.addAttribute("favorites", favorites);
        return "favorites/list";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> add(@RequestParam Long masterId, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        User master = userService.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));

        if (master.getRole() != Role.MASTER) {
            response.put("success", false);
            response.put("message", "Некорректный ID мастера");
            return ResponseEntity.badRequest().body(response);
        }

        if (favoriteMasterService.isFavorite(customer, master)) {
            response.put("success", false);
            response.put("message", "Мастер уже в избранном");
            return ResponseEntity.ok(response);
        } else {
            favoriteMasterService.add(customer, master, null);
            response.put("success", true);
            response.put("message", "Мастер добавлен в избранное");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> remove(@RequestParam Long masterId, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        User master = userService.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));

        favoriteMasterService.remove(customer, master);
        response.put("success", true);
        response.put("message", "Мастер удалён из избранного");
        return ResponseEntity.ok(response);
    }
}
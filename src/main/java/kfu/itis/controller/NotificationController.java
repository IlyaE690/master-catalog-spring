package kfu.itis.controller;

import kfu.itis.model.entity.Notification;
import kfu.itis.model.entity.User;
import kfu.itis.service.NotificationService;
import kfu.itis.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public String notifications(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<Notification> notifications = notificationService.findByUser(user);
        List<Notification> unreadNotifications = notificationService.findUnreadByUser(user);

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadNotifications.size());

        return "notifications/list";
    }

    @PostMapping("/{id}/read")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/read-all")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markAllAsRead(Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        notificationService.markAllAsRead(user);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
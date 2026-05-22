package kfu.itis.controller;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Review;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.OrderStatus;
import kfu.itis.model.enums.Role;
import kfu.itis.service.OrderService;
import kfu.itis.service.ReviewService;
import kfu.itis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;
    private final ReviewService reviewService;

    public AdminController(UserService userService, OrderService orderService, ReviewService reviewService) {
        this.userService = userService;
        this.orderService = orderService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("totalMasters", userService.findAllMasters().size());
        model.addAttribute("totalCustomers", userService.findAllByRole(Role.CUSTOMER).size());
        model.addAttribute("totalOrders", orderService.findAll().size());
        model.addAttribute("pendingOrders", orderService.findAllByStatus(OrderStatus.NEW).size());
        model.addAttribute("totalReviews", reviewService.findAll().size());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(@RequestParam(required = false) String role, Model model) {
        List<User> users;
        if (role != null && !role.isEmpty()) {
            users = userService.findAllByRole(Role.valueOf(role));
            model.addAttribute("selectedRole", role);
        } else {
            users = userService.findAll();
        }
        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values());
        return "admin/users";
    }

    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id).orElseThrow();
        user.setEnabled(!user.isEnabled());
        userService.save(user);
        redirectAttributes.addFlashAttribute("success",
                user.isEnabled() ? "Пользователь разблокирован" : "Пользователь заблокирован");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam Role role, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id).orElseThrow();
        user.setRole(role);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Роль изменена на " + role);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id).orElseThrow();
        String username = user.getUsername();
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Пользователь '" + username + "' удалён");
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(required = false) String status, Model model) {
        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderService.findAllByStatus(OrderStatus.valueOf(status));
            model.addAttribute("selectedStatus", status);
        } else {
            orders = orderService.findAll();
        }
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String changeOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status,
                                    RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(id).orElseThrow();

        switch (status) {
            case ASSIGNED:
                order.setStatus(OrderStatus.ASSIGNED);
                orderService.update(order);
                break;
            case IN_PROGRESS:
                order.setStatus(OrderStatus.IN_PROGRESS);
                orderService.update(order);
                break;
            case COMPLETED:
                orderService.complete(id);
                break;
            case CANCELLED:
                orderService.cancel(id);
                break;
            default:
                order.setStatus(status);
                orderService.update(order);
        }

        redirectAttributes.addFlashAttribute("success", "Статус заказа #" + id + " изменён на " + status);
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        orderService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Заказ #" + id + " удалён");
        return "redirect:/admin/orders";
    }

    @GetMapping("/reviews")
    public String reviews(Model model) {
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "admin/reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reviewService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Отзыв удалён");
        return "redirect:/admin/reviews";
    }
}

package kfu.itis.controller;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Review;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.OrderStatus;
import kfu.itis.service.OrderService;
import kfu.itis.service.ReviewService;
import kfu.itis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderService orderService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, OrderService orderService, UserService userService) {
        this.reviewService = reviewService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/create/{orderId}")
    public String reviewForm(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderWithDetailsForReview(orderId);
        model.addAttribute("order", order);
        return "reviews/create";
    }

    @PostMapping("/create/{orderId}")
    public String createReview(@PathVariable Long orderId,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            redirectAttributes.addFlashAttribute("error", "Отзыв можно оставить только после выполнения заказа");
            return "redirect:/orders/" + orderId;
        }

        if (reviewService.findByOrder(order).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Отзыв для этого заказа уже оставлен");
            return "redirect:/orders/" + orderId;
        }

        User author = userService.findByUsername(principal.getName()).orElseThrow();
        User targetUser = order.getMaster();

        if (targetUser == null) {
            redirectAttributes.addFlashAttribute("error", "Заказ не был назначен мастеру");
            return "redirect:/orders/" + orderId;
        }

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setAuthor(author);
        review.setTargetUser(targetUser);
        review.setOrder(order);

        reviewService.create(review);
        redirectAttributes.addFlashAttribute("success", "Отзыв успешно оставлен");
        return "redirect:/orders/" + orderId;
    }

    @GetMapping("/master/{masterId}")
    public String masterReviews(@PathVariable Long masterId, Model model) {
        User master = userService.findByIdWithSpecializations(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));

        List<Review> reviews = reviewService.findByTargetUserWithAuthor(master);

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        model.addAttribute("master", master);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewsCount", reviews.size());

        return "reviews/master-reviews";
    }
}

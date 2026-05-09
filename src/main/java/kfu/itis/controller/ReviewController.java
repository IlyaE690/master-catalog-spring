package kfu.itis.controller;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Review;
import kfu.itis.model.entity.User;
import kfu.itis.service.OrderService;
import kfu.itis.service.ReviewService;
import kfu.itis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
        Order order = orderService.findById(orderId).orElseThrow();
        model.addAttribute("order", order);
        return "reviews/create";
    }

    @PostMapping("/create/{orderId}")
    public String createReview(@PathVariable Long orderId,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               Principal principal) {
        Order order = orderService.findById(orderId).orElseThrow();
        User author = userService.findByUsername(principal.getName()).orElseThrow();
        User targetUser = order.getMaster();

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setAuthor(author);
        review.setTargetUser(targetUser);
        review.setOrder(order);

        reviewService.create(review);
        return "redirect:/orders/" + orderId;
    }
}

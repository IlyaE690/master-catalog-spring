package kfu.itis.controller;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.service.CurrencyService;
import kfu.itis.service.ImageStorageService;
import kfu.itis.service.OrderService;
import kfu.itis.service.SpecializationService;

import kfu.itis.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final SpecializationService specializationService;
    private final CurrencyService currencyService;
    private final ImageStorageService imageStorageService;
    @Value("${app.yandex.maps-api-key:}")
    private String yandexMapsApiKey;

    public OrderController(OrderService orderService,  UserService userService, SpecializationService specializationService,
                           CurrencyService currencyService,
                           ImageStorageService imageStorageService) {
        this.orderService = orderService;
        this.userService = userService;
        this.specializationService = specializationService;
        this.currencyService = currencyService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping("/new")
    public String newOrderForm(Model model) {
        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("yandexApiKey", yandexMapsApiKey);
        return "orders/create";
    }

    @PostMapping("/new")
    public String createOrder(@RequestParam Long specializationId,
                              @RequestParam String title,
                              @RequestParam String description,
                              @RequestParam String address,
                              @RequestParam LocalDateTime scheduledDate,
                              @RequestParam(required = false) MultipartFile orderPhoto,
                              Principal principal) {
        User customer = userService.findByUsername(principal.getName()).orElseThrow();

        Specialization specialization = specializationService.findById(specializationId).orElseThrow();

        Order order = new Order();

        order.setCustomer(customer);
        order.setSpecialization(specialization);
        order.setTitle(title);
        order.setDescription(description);
        order.setAddress(address);
        order.setScheduledDate(scheduledDate);
        order.setImageUrl(imageStorageService.uploadOrderImage(orderPhoto));

        orderService.create(order);
        return "redirect:/orders/my";
    }

    @GetMapping("/my")
    public String myOrders(Principal principal, Model model) {
        User customer = userService.findByUsername(principal.getName()).orElseThrow();

        model.addAttribute("orders", orderService.findByCustomer(customer));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model, Principal principal) {
        Order order = orderService.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден"));

        model.addAttribute("order", order);
        model.addAttribute("isCustomer", order.getCustomer().getUsername().equals(principal.getName()));
        model.addAttribute("isMaster", order.getMaster() != null && order.getMaster().getUsername().equals(principal.getName()));
        model.addAttribute("priceInUsd", currencyService.convertRubToUsd(order.getPrice()));

        return "orders/details";
    }

    @GetMapping("/available")
    public String availableOrders(Principal principal, Model model) {
        User master = userService.findByUsername(principal.getName()).orElseThrow();

        model.addAttribute("orders", orderService.findNewOrdersForMaster(master.getId()));

        return "orders/available";
    }

    @PostMapping("/{id}/accept")
    public String acceptOrder(@PathVariable Long id, Principal principal) {
        User master = userService.findByUsername(principal.getName()).orElseThrow();

        orderService.assignMaster(id, master);

        return "redirect:/orders/assigned";
    }

    @GetMapping("/assigned")
    public String assignedOrders(Principal principal, Model model) {
        User master = userService.findByUsername(principal.getName()).orElseThrow();

        model.addAttribute("orders", orderService.findByMaster(master));

        return "orders/assigned";
    }

    @PostMapping("/{id}/start")
    public String startOrder(@PathVariable Long id) {
        orderService.startWork(id);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/complete")
    public String completeOrder(@PathVariable Long id) {
        orderService.complete(id);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {
        orderService.cancel(id);
        return "redirect:/orders/my";
    }


}

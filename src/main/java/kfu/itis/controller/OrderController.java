package kfu.itis.controller;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.OrderStatus;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public OrderController(OrderService orderService, UserService userService,
                           SpecializationService specializationService,
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

    @GetMapping("/create-for-master/{masterId}")
    public String createOrderForMaster(@PathVariable Long masterId, Model model, Principal principal) {
        User master = userService.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));

        model.addAttribute("targetMasterId", masterId);
        model.addAttribute("targetMasterName", (master.getFirstName() != null ? master.getFirstName() : "") + " " + (master.getLastName() != null ? master.getLastName() : ""));
        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("yandexApiKey", yandexMapsApiKey);
        return "orders/create-for-master";
    }

    @PostMapping("/create-with-master")
    public String createOrderWithMaster(@RequestParam Long specializationId,
                                        @RequestParam Long masterId,
                                        @RequestParam String title,
                                        @RequestParam String description,
                                        @RequestParam String address,
                                        @RequestParam LocalDateTime scheduledDate,
                                        @RequestParam(required = false) MultipartFile orderPhoto,
                                        Principal principal,
                                        RedirectAttributes redirectAttributes) {

        if (scheduledDate.isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Дата выполнения не может быть в прошлом");
            return "redirect:/orders/create-for-master/" + masterId;
        }

        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Specialization specialization = specializationService.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Специализация не найдена"));

        User master = userService.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setSpecialization(specialization);
        order.setMaster(master);
        order.setTitle(title);
        order.setDescription(description);
        order.setAddress(address);
        order.setScheduledDate(scheduledDate);
        order.setStatus(OrderStatus.ASSIGNED);

        if (orderPhoto != null && !orderPhoto.isEmpty()) {
            String imageUrl = imageStorageService.uploadOrderImage(orderPhoto);
            order.setImageUrl(imageUrl);
        }

        Order savedOrder = orderService.create(order);

        redirectAttributes.addFlashAttribute("success", "Заказ создан и отправлен мастеру!");
        return "redirect:/orders/" + savedOrder.getId();
    }

    @PostMapping("/new")
    public String createOrder(@RequestParam Long specializationId,
                              @RequestParam String title,
                              @RequestParam String description,
                              @RequestParam String address,
                              @RequestParam LocalDateTime scheduledDate,
                              @RequestParam(required = false) MultipartFile orderPhoto,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {

        if (scheduledDate.isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Дата выполнения не может быть в прошлом");
            return "redirect:/orders/new";
        }

        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Specialization specialization = specializationService.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Специализация не найдена"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setSpecialization(specialization);
        order.setTitle(title);
        order.setDescription(description);
        order.setAddress(address);
        order.setScheduledDate(scheduledDate);

        if (orderPhoto != null && !orderPhoto.isEmpty()) {
            String imageUrl = imageStorageService.uploadOrderImage(orderPhoto);
            order.setImageUrl(imageUrl);
        }

        orderService.create(order);
        redirectAttributes.addFlashAttribute("success", "Заказ успешно создан!");
        return "redirect:/orders/my";
    }

    @GetMapping("/my")
    public String myOrders(Principal principal, Model model) {
        User customer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        model.addAttribute("orders", orderService.findByCustomer(customer));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model, Principal principal) {
        Order order = orderService.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        model.addAttribute("order", order);
        model.addAttribute("scheduledDateStr", order.getScheduledDate() != null ? order.getScheduledDate().format(formatter) : "");
        model.addAttribute("createdAtStr", order.getCreatedAt() != null ? order.getCreatedAt().format(formatter) : "");
        model.addAttribute("isCustomer", order.getCustomer().getUsername().equals(principal.getName()));
        model.addAttribute("isMaster", order.getMaster() != null && order.getMaster().getUsername().equals(principal.getName()));

        if (order.getPrice() != null) {
            model.addAttribute("priceInUsd", currencyService.convertRubToUsd(order.getPrice()));
        } else {
            model.addAttribute("priceInUsd", null);
        }

        return "orders/details";
    }

    @GetMapping("/available")
    public String availableOrders(Principal principal, Model model) {
        User master = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        model.addAttribute("orders", orderService.findNewOrdersForMaster(master.getId()));
        return "orders/available";
    }

    @PostMapping("/{id}/accept")
    public String acceptOrder(@PathVariable Long id, Principal principal) {
        User master = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        orderService.assignMaster(id, master);
        return "redirect:/orders/assigned";
    }

    @GetMapping("/assigned")
    public String assignedOrders(Principal principal, Model model) {
        User master = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        model.addAttribute("orders", orderService.findByMaster(master));
        return "orders/assigned";
    }

    @PostMapping("/{id}/start")
    public String startOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.startWork(id);
            redirectAttributes.addFlashAttribute("success", "Работа над заказом начата");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/complete")
    public String completeOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.complete(id);
            redirectAttributes.addFlashAttribute("success", "Заказ завершён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancel(id);
            redirectAttributes.addFlashAttribute("success", "Заказ отменён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/my";
    }

    @PostMapping("/{id}/reject")
    public String rejectOrder(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User master = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            Order order = orderService.findById(id).orElseThrow();

            if (order.getMaster() == null || !order.getMaster().getId().equals(master.getId())) {
                redirectAttributes.addFlashAttribute("error", "Вы не можете отказаться от этого заказа");
                return "redirect:/orders/assigned";
            }

            orderService.reject(id);
            redirectAttributes.addFlashAttribute("success", "Вы отказались от заказа");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/assigned";
    }
}
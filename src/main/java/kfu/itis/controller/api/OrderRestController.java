package kfu.itis.controller.api;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.service.OrderService;
import kfu.itis.service.SpecializationService;
import kfu.itis.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final UserService userService;
    private final SpecializationService specializationService;

    public OrderRestController(OrderService orderService, UserService userService, SpecializationService specializationService) {
        this.orderService = orderService;
        this.userService = userService;
        this.specializationService = specializationService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();

        List<Order> orders = orderService.findByCustomer(user);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден"));
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> body, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();

        Long specializationId = Long.valueOf(body.get("specializationId").toString());
        Specialization specialization = specializationService.findById(specializationId).orElseThrow();

        Order order = new Order();
        order.setCustomer(user);
        order.setSpecialization(specialization);
        order.setTitle((String) body.get("title"));
        order.setDescription((String) body.get("description"));
        order.setAddress((String) body.get("address"));
        order.setScheduledDate(java.time.LocalDateTime.parse((String) body.get("scheduledDate")));

        Order created = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Order order = orderService.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден"));

        if (body.containsKey("title")) {
            order.setTitle((String) body.get("title"));
        }
        if (body.containsKey("description")) {
            order.setDescription((String) body.get("description"));
        }
        if (body.containsKey("address")) {
            order.setAddress((String) body.get("address"));
        }

        Order updated = orderService.update(order);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody Map<String, String> body,
                                          Principal principal) {
        String status = body.get("status");
        User user = userService.findByUsername(principal.getName()).orElseThrow();

        return switch (status) {
            case "ASSIGNED" -> ResponseEntity.ok(orderService.assignMaster(id, user));
            case "IN_PROGRESS" -> ResponseEntity.ok(orderService.startWork(id));
            case "COMPLETED" -> ResponseEntity.ok(orderService.complete(id));
            case "CANCELLED" -> ResponseEntity.ok(orderService.cancel(id));
            default -> ResponseEntity.badRequest().body(Map.of("error", "Неверный статус: " + status));
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        orderService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<Order>> getAvailableOrders(Principal principal) {
        User master = userService.findByUsername(principal.getName()).orElseThrow();
        List<Order> orders = orderService.findNewOrdersForMaster(master.getId());
        return ResponseEntity.ok(orders);
    }
}

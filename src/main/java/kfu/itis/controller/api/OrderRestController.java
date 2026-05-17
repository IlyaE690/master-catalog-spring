package kfu.itis.controller.api;

import jakarta.validation.Valid;
import kfu.itis.mapper.OrderMapper;
import kfu.itis.model.dto.OrderCreateRequestDto;
import kfu.itis.model.dto.OrderResponseDto;
import kfu.itis.model.dto.OrderStatusUpdateRequestDto;
import kfu.itis.model.dto.OrderUpdateRequestDto;
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
    private final OrderMapper orderMapper;

    public OrderRestController(OrderService orderService, UserService userService,
                               SpecializationService specializationService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.specializationService = specializationService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(orderService.findByCustomer(user).stream().map(orderMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        Order order = orderService.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден"));
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderCreateRequestDto body, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        Specialization specialization = specializationService.findById(body.specializationId()).orElseThrow();

        Order order = new Order();
        order.setCustomer(user);
        order.setSpecialization(specialization);
        order.setTitle(body.title());
        order.setDescription(body.description());
        order.setAddress(body.address());
        order.setScheduledDate(body.scheduledDate());

        if (body.targetMasterId() != null) {
            User targetMaster = userService.findById(body.targetMasterId()).orElseThrow();
            order.setMaster(targetMaster);
        }

        Order created = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toDto(created));

    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id, @RequestBody OrderUpdateRequestDto body) {
        Order order = orderService.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден"));

        if (body.title() != null) {
            order.setTitle(body.title());
        }
        if (body.description() != null) {
            order.setDescription(body.description());
        }
        if (body.address() != null) {
            order.setAddress(body.address());
        }

        Order updated = orderService.update(order);
        return ResponseEntity.ok(orderMapper.toDto(updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody @Valid OrderStatusUpdateRequestDto body,
                                          Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();

        return switch (body.status()) {
            case "ASSIGNED" -> ResponseEntity.ok(orderMapper.toDto(orderService.assignMaster(id, user)));
            case "IN_PROGRESS" -> ResponseEntity.ok(orderMapper.toDto(orderService.startWork(id)));
            case "COMPLETED" -> ResponseEntity.ok(orderMapper.toDto(orderService.complete(id)));
            case "CANCELLED" -> ResponseEntity.ok(orderMapper.toDto(orderService.cancel(id)));
            default -> ResponseEntity.badRequest().body(Map.of("error", "Неверный статус: " + body.status()));
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        orderService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<OrderResponseDto>> getAvailableOrders(Principal principal) {
        User master = userService.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(orderService.findNewOrdersForMaster(master.getId()).stream().map(orderMapper::toDto).toList());
    }
}

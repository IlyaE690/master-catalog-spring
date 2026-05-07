package kfu.itis.service.impl;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.OrderStatus;
import kfu.itis.repository.OrderRepository;
import kfu.itis.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomer(User customer) {
        return orderRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByMaster(User master) {
        return orderRepository.findByMasterOrderByCreatedAtDesc(master);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerAndStatus(User customer, OrderStatus status) {
        return orderRepository.findByCustomerAndStatusOrderByCreatedAtDesc(customer, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByMasterAndStatus(User master, OrderStatus status) {
        return orderRepository.findByMasterAndStatusOrderByCreatedAtDesc(master, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findNewOrdersForMaster(Long masterId) {
        return orderRepository.findNewOrdersForMaster(masterId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findOrdersByFilters(Long specializationId, OrderStatus status,
                                           BigDecimal minPrice, BigDecimal maxPrice,
                                           String cityInAddress) {
        return orderRepository.findOrdersByFilters(specializationId, status, minPrice, maxPrice, cityInAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findHighCostMasters() {
        return orderRepository.findHighCostMasters();
    }

    @Override
    @Transactional(readOnly = true)
    public long countCompletedByMaster(User master) {
        return orderRepository.countByMasterAndStatus(master, OrderStatus.COMPLETED);
    }

    @Override
    @Transactional
    public Order create(Order order) {
        order.setStatus(OrderStatus.NEW);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order update(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order assignMaster(Long orderId, User master) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setMaster(master);
        order.setStatus(OrderStatus.ASSIGNED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order startWork(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setStatus(OrderStatus.IN_PROGRESS);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order complete(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}

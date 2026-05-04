package kfu.itis.service;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<Order> findById(Long id);

    List<Order> findByCustomer(User customer);

    List<Order> findByMaster(User master);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findNewOrdersForMaster(Long masterId);

    List<User> findHighCostMasters();

    Order create(Order order);

    Order update(Order order);

    Order assignMaster(Long orderId, User master);

    Order updateStatus(Long orderId, OrderStatus status);

    Order cancel(Long orderId);
}
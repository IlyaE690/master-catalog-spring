package kfu.itis.service;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<Order> findById(Long id);

    List<Order> findByCustomer(User customer);

    List<Order> findByMaster(User master);

    List<Order> findByCustomerAndStatus(User customer, OrderStatus status);

    List<Order> findByMasterAndStatus(User master, OrderStatus status);

    List<Order> findNewOrdersForMaster(Long masterId);

    List<Order> findOrdersByFilters(Long specializationId, OrderStatus status,
                                    BigDecimal minPrice, BigDecimal maxPrice,
                                    String cityInAddress);

    List<User> findHighCostMasters();

    long countCompletedByMaster(User master);

    Order create(Order order);

    Order update(Order order);

    Order assignMaster(Long orderId, User master);

    Order startWork(Long orderId);

    Order complete(Long orderId);

    Order cancel(Long orderId);

    List<Order> findAll();

    List<Order> findAllByStatus(OrderStatus status);

    void deleteById(Long id);

    Optional<Order> findByIdWithDetails(Long id);

    Order reject(Long orderId);
}
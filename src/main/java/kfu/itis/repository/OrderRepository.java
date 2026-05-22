package kfu.itis.repository;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.OrderStatus;
import kfu.itis.repository.custom.CustomOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {

    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);

    List<Order> findByMasterOrderByCreatedAtDesc(User master);

    List<Order> findByCustomerAndStatusOrderByCreatedAtDesc(User customer, OrderStatus status);

    List<Order> findByMasterAndStatusOrderByCreatedAtDesc(User master, OrderStatus status);

    long countByMasterAndStatus(User master, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);

    // Новые заказы для мастера по его специализациям
    @Query("SELECT o FROM Order o WHERE o.status = 'NEW' AND o.specialization IN " +
            "(SELECT s FROM User u JOIN u.specializations s WHERE u.id = :masterId) " +
            "ORDER BY o.createdAt DESC")
    List<Order> findNewOrdersForMaster(@Param("masterId") Long masterId);

    // Мастера с чеком выше среднего (премиум-мастера)
    @Query("SELECT DISTINCT o.master FROM Order o " +
            "WHERE o.master IS NOT NULL " +
            "GROUP BY o.master " +
            "HAVING AVG(o.price) > (SELECT AVG(o2.price) FROM Order o2 WHERE o2.master IS NOT NULL)")
    List<User> findHighCostMasters();
}
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
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.specialization " +
            "LEFT JOIN FETCH o.customer " +
            "WHERE o.customer = :customer " +
            "ORDER BY o.createdAt DESC")
    List<Order> findByCustomerOrderByCreatedAtDesc(@Param("customer") User customer);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.specialization " +
            "LEFT JOIN FETCH o.master " +
            "WHERE o.master = :master " +
            "ORDER BY o.createdAt DESC")
    List<Order> findByMasterOrderByCreatedAtDesc(@Param("master") User master);

    List<Order> findByCustomerAndStatusOrderByCreatedAtDesc(User customer, OrderStatus status);

    List<Order> findByMasterAndStatusOrderByCreatedAtDesc(User master, OrderStatus status);

    long countByMasterAndStatus(User master, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.specialization " +
            "LEFT JOIN FETCH o.customer " +
            "LEFT JOIN FETCH o.master " +
            "WHERE o.status = 'NEW' AND o.specialization IN " +
            "(SELECT s FROM User u JOIN u.specializations s WHERE u.id = :masterId) " +
            "ORDER BY o.createdAt DESC")
    List<Order> findNewOrdersForMaster(@Param("masterId") Long masterId);

    @Query("SELECT DISTINCT o.master FROM Order o " +
            "WHERE o.master IS NOT NULL " +
            "GROUP BY o.master " +
            "HAVING AVG(o.price) > (SELECT AVG(o2.price) FROM Order o2 WHERE o2.master IS NOT NULL)")
    List<User> findHighCostMasters();

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.customer " +
            "LEFT JOIN FETCH o.master " +
            "LEFT JOIN FETCH o.specialization " +
            "WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);
}
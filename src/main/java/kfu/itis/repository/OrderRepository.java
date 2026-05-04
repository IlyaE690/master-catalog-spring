package kfu.itis.repository;

import kfu.itis.model.entity.Order;
import kfu.itis.model.enums.OrderStatus;
import kfu.itis.model.entity.User;
import kfu.itis.repository.custom.CustomOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>, CustomOrderRepository {
    List<Order> findByCustomer(User customer);

    List<Order> findByMaster(User customer);

    List<Order> findByCustomerAndMaster(User customer, User master);

    List<Order> findByStatusAndCustomer(OrderStatus status, User customer);

    List<Order> findBySpecializationIdAndStatus(Long specializationId, OrderStatus status);

    //все заказы со статусом new у мастера
    @Query("SELECT o FROM Order o WHERE o.status = 'NEW' AND o.specialization IN " +
            "(SELECT s FROM User u JOIN u.specializations s WHERE u.id = :masterId)")
    List<Order> findNewOrdersForMasterSpecializations(@Param("masterId") Long masterId);

    //мастера с высоким средним ценником
    @Query("SELECT DISTINCT o.master FROM Order o " +
            "WHERE o.master IS NOT NULL " +
            "GROUP BY o.master " +
            "HAVING AVG(o.price) > (SELECT AVG(o2.price) FROM Order o2 WHERE o2.master IS NOT NULL)")
    List<User> findHighCostMaster();
}

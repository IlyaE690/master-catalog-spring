package kfu.itis.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import kfu.itis.model.entity.Order;
import kfu.itis.model.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Order> findOrdersByFilters(Long specializationId, OrderStatus status,
                                           BigDecimal minPrice, BigDecimal maxPrice,
                                           String cityInAddress) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> order = query.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        if (specializationId != null) {
            predicates.add(cb.equal(order.get("specialization").get("id"), specializationId));
        }
        if (status != null) {
            predicates.add(cb.equal(order.get("status"), status));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(order.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(order.get("price"), maxPrice));
        }
        if (cityInAddress != null && !cityInAddress.isBlank()) {
            predicates.add(cb.like(cb.lower(order.get("address")),
                    "%" + cityInAddress.toLowerCase() + "%"));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(order.get("createdAt")));

        return entityManager.createQuery(query).getResultList();
    }
}

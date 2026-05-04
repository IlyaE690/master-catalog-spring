package kfu.itis.repository.custom;

import kfu.itis.model.entity.Order;
import kfu.itis.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public interface CustomOrderRepository {
    List<Order> findOrdersByFilters(Long specializationId, OrderStatus status,
                                    BigDecimal minPrice, BigDecimal maxPrice,
                                    String cityInAddress);
}

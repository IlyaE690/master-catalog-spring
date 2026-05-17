package kfu.itis.mapper;

import kfu.itis.model.dto.OrderResponseDto;
import kfu.itis.model.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponseDto toDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getTitle(),
                order.getDescription(),
                order.getAddress(),
                order.getScheduledDate(),
                order.getStatus(),
                order.getPrice(),
                order.getCustomer().getId(),
                order.getCustomer().getUsername(),
                order.getMaster() != null ? order.getMaster().getId() : null,
                order.getMaster() != null ? order.getMaster().getUsername() : null,
                order.getSpecialization().getId(),
                order.getSpecialization().getName()
        );
    }
}

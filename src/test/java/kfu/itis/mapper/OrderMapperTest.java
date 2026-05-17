package kfu.itis.mapper;

import kfu.itis.model.dto.OrderResponseDto;
import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {
    private final OrderMapper mapper = new OrderMapper();

    @Test
    void toDto_mapsRequiredFields() {
        User customer = User.builder().id(1L).username("cust").build();
        User master = User.builder().id(2L).username("mast").build();
        Specialization specialization = Specialization.builder().id(3L).name("Электрик").build();
        Order order = Order.builder().id(10L).customer(customer).master(master).specialization(specialization)
                .title("Тест").description("Desc").address("Казань")
                .scheduledDate(LocalDateTime.now().plusDays(1)).status(OrderStatus.NEW).build();

        OrderResponseDto dto = mapper.toDto(order);
        assertThat(dto.customerUsername()).isEqualTo("cust");
        assertThat(dto.masterUsername()).isEqualTo("mast");
        assertThat(dto.specializationName()).isEqualTo("Электрик");
    }
}

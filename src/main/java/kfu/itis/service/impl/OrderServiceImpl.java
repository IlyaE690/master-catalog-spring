package kfu.itis.service.impl;

import kfu.itis.model.entity.Notification;
import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.NotificationType;
import kfu.itis.model.enums.OrderStatus;
import kfu.itis.repository.OrderRepository;
import kfu.itis.service.NotificationService;
import kfu.itis.service.OrderService;
import kfu.itis.service.WeatherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    private final WeatherService weatherService;

    public OrderServiceImpl(OrderRepository orderRepository, NotificationService notificationService, WeatherService weatherService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
        this.weatherService = weatherService;
    }

    private void createNotification(User user, String title, String message, Long orderId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedOrderId(orderId);
        notification.setType(NotificationType.STATUS_CHANGED);
        notificationService.create(notification);
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

        boolean badWeather = weatherService.isBadWeatherNow();
        double weatherCoefficient = 1.0;

        if (Boolean.TRUE.equals(order.getSpecialization().getWeatherDependent()) && badWeather) {
            weatherCoefficient = weatherService.getWeatherCoefficientForOutdoorWorks();
            if (order.getMaster() != null && order.getMaster().getBadWeatherPriceCoefficient() != null) {
                weatherCoefficient *= order.getMaster().getBadWeatherPriceCoefficient();
            }
        }

        order.setWeatherCoefficient(weatherCoefficient);
        if (order.getPrice() == null && order.getSpecialization().getBasePrice() != null) {
            order.setPrice(order.getSpecialization().getBasePrice().multiply(BigDecimal.valueOf(weatherCoefficient)));
        }

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
        Order saved = orderRepository.save(order);

        createNotification(order.getCustomer(),
                "Мастер назначен",
                "Мастер " + master.getFirstName() + " " + master.getLastName() + " принял ваш заказ <<" + order.getTitle() + ">>",
                order.getId());
        return saved;
    }

    @Override
    @Transactional
    public Order startWork(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setStatus(OrderStatus.IN_PROGRESS);
        Order saved = orderRepository.save(order);

        createNotification(
                order.getCustomer(),
                "Мастер начал работу",
                "Мастер приступил к выполнению заказа <<" + order.getTitle() + ">>",
                order.getId()
        );

        return saved;
    }

    @Override
    @Transactional
    public Order complete(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);

        createNotification(
                order.getCustomer(),
                "Заказ выполнен",
                "Мастер завершил выполнение заказа «" + order.getTitle() + "». Оставьте отзыв!",
                order.getId()
        );

        return saved;
    }

    @Override
    @Transactional
    public Order cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);

        if (order.getMaster() != null) {
            createNotification(
                    order.getMaster(),
                    "Заказ отменён",
                    "Заказчик отменил заказ «" + order.getTitle() + "»",
                    order.getId()
            );
        }

        return saved;
    }
}

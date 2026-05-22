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

    private void createNotification(User user, String title, String message, Long orderId, NotificationType type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedOrderId(orderId);
        notification.setType(type);
        notificationService.create(notification);
    }

    private void recalculatePrice(Order order) {
        if (order.getSpecialization() == null) {
            return;
        }

        boolean badWeather = weatherService.isBadWeather(order.getScheduledDate());
        double weatherCoefficient = 1.0;

        if (Boolean.TRUE.equals(order.getSpecialization().getWeatherDependent()) && badWeather) {
            weatherCoefficient = weatherService.getWeatherCoefficientForOutdoorWorks(order.getScheduledDate());
            if (order.getMaster() != null && order.getMaster().getBadWeatherPriceCoefficient() != null) {
                weatherCoefficient *= order.getMaster().getBadWeatherPriceCoefficient();
            }
        }

        order.setWeatherCoefficient(weatherCoefficient);
        if (order.getSpecialization().getBasePrice() != null) {
            order.setPrice(order.getSpecialization().getBasePrice().multiply(BigDecimal.valueOf(weatherCoefficient)));
        }
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
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.NEW);
        }
        recalculatePrice(order);
        Order saved = orderRepository.save(order);

        if (order.getMaster() != null) {
            createNotification(order.getMaster(),
                    "Новый заказ",
                    "Вам назначен новый заказ: " + order.getTitle(),
                    order.getId(),
                    NotificationType.ORDER_CREATED);
        }

        createNotification(order.getCustomer(),
                "Заказ создан",
                "Ваш заказ #" + order.getId() + " успешно создан",
                order.getId(),
                NotificationType.ORDER_CREATED);

        return saved;
    }

    @Override
    @Transactional
    public Order update(Order order) {
        recalculatePrice(order);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order assignMaster(Long orderId, User master) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setMaster(master);
        order.setStatus(OrderStatus.ASSIGNED);
        recalculatePrice(order);
        Order saved = orderRepository.save(order);

        createNotification(order.getCustomer(),
                "Мастер назначен",
                "Мастер " + master.getFirstName() + " " + master.getLastName() + " принял ваш заказ",
                order.getId(),
                NotificationType.MASTER_ASSIGNED);

        createNotification(master,
                "Заказ принят",
                "Вы приняли заказ: " + order.getTitle(),
                order.getId(),
                NotificationType.MASTER_ASSIGNED);

        return saved;
    }

    @Override
    @Transactional
    public Order startWork(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setStatus(OrderStatus.IN_PROGRESS);
        Order saved = orderRepository.save(order);

        createNotification(order.getCustomer(),
                "Работа начата",
                "Мастер приступил к выполнению заказа",
                order.getId(),
                NotificationType.STATUS_CHANGED);

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

        createNotification(order.getCustomer(),
                "Заказ выполнен",
                "Мастер завершил выполнение заказа. Оставьте отзыв!",
                order.getId(),
                NotificationType.STATUS_CHANGED);

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
            createNotification(order.getMaster(),
                    "Заказ отменён",
                    "Заказчик отменил заказ",
                    order.getId(),
                    NotificationType.STATUS_CHANGED);
        }

        createNotification(order.getCustomer(),
                "Заказ отменён",
                "Вы отменили заказ",
                order.getId(),
                NotificationType.STATUS_CHANGED);

        return saved;
    }

    @Override
    @Transactional
    public Order reject(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        order.setStatus(OrderStatus.NEW);
        order.setMaster(null);
        Order saved = orderRepository.save(order);

        createNotification(order.getCustomer(),
                "Мастер отказался",
                "Мастер отказался от выполнения заказа. Заказ снова доступен другим мастерам.",
                order.getId(),
                NotificationType.STATUS_CHANGED);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAllByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findByIdWithDetails(Long id) {
        return orderRepository.findByIdWithDetails(id);
    }
}
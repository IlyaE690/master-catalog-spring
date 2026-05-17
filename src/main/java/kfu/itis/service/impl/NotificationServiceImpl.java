package kfu.itis.service.impl;

import kfu.itis.model.dto.NotificationMessageDto;
import kfu.itis.model.entity.Notification;
import kfu.itis.model.entity.User;
import kfu.itis.repository.NotificationRepository;
import kfu.itis.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> findByUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> findUnreadByUser(User user) {
        return notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
    }

    @Override
    @Transactional
    public Notification create(Notification notification) {
        Notification saved = notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(
                saved.getUser().getUsername(),
                "/queue/notifications",
                new NotificationMessageDto(saved.getId(), saved.getTitle(), saved.getMessage(), saved.getRelatedOrderId(), saved.getCreatedAt())
        );
        return saved;
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }

    @Override
    @Transactional
    public void markAllAsRead(User user) {
        List<Notification> unread = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
    }
}

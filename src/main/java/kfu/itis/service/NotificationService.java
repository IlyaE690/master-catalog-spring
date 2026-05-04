package kfu.itis.service;

import kfu.itis.model.entity.Notification;
import kfu.itis.model.entity.User;

import java.util.List;

public interface NotificationService {

    List<Notification> findByUser(User user);

    List<Notification> findUnreadByUser(User user);

    int countUnreadByUser(User user);

    Notification create(Notification notification);

    void markAsRead(Long notificationId);

    void markAllAsRead(User user);
}
package kfu.itis.service;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Review;
import kfu.itis.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Optional<Review> findByOrder(Order order);

    List<Review> findByTargetUser(User targetUser);

    List<Review> findByAuthor(User author);

    Review create(Review review);

    List<Review> findAll();

    void deleteById(Long id);
}
package kfu.itis.service.impl;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Review;
import kfu.itis.model.entity.User;
import kfu.itis.repository.ReviewRepository;
import kfu.itis.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findByOrder(Order order) {
        return reviewRepository.findByOrder(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findByTargetUser(User targetUser) {
        return reviewRepository.findByTargetUserOrderByCreatedAtDesc(targetUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findByAuthor(User author) {
        return reviewRepository.findByAuthorOrderByCreatedAtDesc(author);
    }

    @Override
    @Transactional
    public Review create(Review review) {
        return reviewRepository.save(review);
    }
}

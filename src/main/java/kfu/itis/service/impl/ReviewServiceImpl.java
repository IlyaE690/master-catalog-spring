package kfu.itis.service.impl;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Review;
import kfu.itis.model.entity.User;
import kfu.itis.repository.ReviewRepository;
import kfu.itis.repository.UserRepository;
import kfu.itis.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
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
        Review saved = reviewRepository.save(review);

        User master = userRepository.findById(review.getTargetUser().getId())
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));
        updateMasterRating(master);

        return saved;
    }

    private void updateMasterRating(User master) {
        List<Review> reviews = reviewRepository.findByTargetUserOrderByCreatedAtDesc(master);
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        master.setRating(Math.round(averageRating * 100.0) / 100.0);
        userRepository.save(master);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            User master = userRepository.findById(review.getTargetUser().getId())
                    .orElse(null);
            reviewRepository.deleteById(id);
            if (master != null) {
                updateMasterRating(master);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findByTargetUserWithAuthor(User targetUser) {
        List<Review> reviews = reviewRepository.findByTargetUserOrderByCreatedAtDesc(targetUser);

        for (Review review : reviews) {
            if (review.getAuthor() != null) {
                review.getAuthor().getFirstName();
                review.getAuthor().getLastName();
            }
        }

        return reviews;
    }
}
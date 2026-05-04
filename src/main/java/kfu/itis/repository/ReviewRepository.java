package kfu.itis.repository;

import kfu.itis.model.entity.Order;
import kfu.itis.model.entity.Review;
import kfu.itis.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    Optional<Review> findByOrder(Order order);

    List<Review> findByTargetUser(User targetUser);

    List<Review> findByAuthor(User author);
}

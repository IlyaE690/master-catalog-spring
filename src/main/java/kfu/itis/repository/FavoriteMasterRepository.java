// FavoriteMasterRepository.java
package kfu.itis.repository;

import kfu.itis.model.entity.FavoriteMaster;
import kfu.itis.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteMasterRepository extends JpaRepository<FavoriteMaster, Long> {

    List<FavoriteMaster> findByCustomer(User customer);

    Optional<FavoriteMaster> findByCustomerAndMaster(User customer, User master);

    void deleteByCustomerAndMaster(User customer, User master);
}
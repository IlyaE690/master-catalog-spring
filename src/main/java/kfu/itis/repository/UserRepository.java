package kfu.itis.repository;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.specializations WHERE u.role = 'MASTER' ORDER BY u.rating DESC")
    List<User> findByRoleOrderByRatingDesc(Role role);

    List<User> findByRole(Role role);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.specializations WHERE u.role = 'MASTER'")
    List<User> findAllMasters();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.specializations WHERE u.id = :id")
    Optional<User> findByIdWithSpecializations(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.specializations JOIN u.specializations s WHERE s = :specialization AND u.role = 'MASTER'")
    List<User> findAllMastersBySpecialization(@Param("specialization") Specialization specialization);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.specializations WHERE u.role = 'MASTER' AND " +
            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<User> findMastersByName(@Param("query") String query);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.specializations WHERE u.role = 'MASTER' AND u.rating >= :minRating ORDER BY u.rating DESC")
    List<User> findMastersByMinRating(@Param("minRating") Double minRating);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.specializations JOIN u.specializations s WHERE s = :specialization AND u.role = 'MASTER' AND u.rating >= :minRating ORDER BY u.rating DESC")
    List<User> findMastersBySpecializationAndMinRating(@Param("specialization") Specialization specialization,
                                                       @Param("minRating") Double minRating);
}
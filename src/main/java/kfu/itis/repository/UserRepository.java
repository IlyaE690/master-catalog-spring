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

    List<User> findByRoleOrderByRatingDesc(Role role);

    List<User> findByRole(Role role);

    // Поиск мастеров по специализации
    @Query("SELECT u FROM User u JOIN u.specializations s WHERE s = :specialization AND u.role = 'MASTER'")
    List<User> findAllMastersBySpecialization(@Param("specialization") Specialization specialization);

    // Поиск мастеров по имени/фамилии
    @Query("SELECT u FROM User u WHERE u.role = 'MASTER' AND " +
            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<User> findMastersByName(@Param("query") String query);

    // Мастера с рейтингом выше указанного и нужной специализацией
    @Query("SELECT u FROM User u JOIN u.specializations s " +
            "WHERE s = :specialization AND u.role = 'MASTER' AND u.rating >= :minRating " +
            "ORDER BY u.rating DESC")
    List<User> findMastersBySpecializationAndMinRating(@Param("specialization") Specialization specialization,
                                                       @Param("minRating") Double minRating);
}

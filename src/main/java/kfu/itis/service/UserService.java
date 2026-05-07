package kfu.itis.service;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllMasters();

    List<User> findAllMastersBySpecialization(Specialization specialization);

    List<User> findMastersByName(String query);

    List<User> findMastersBySpecializationAndMinRating(Specialization specialization, Double minRating);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User save(User user);
}
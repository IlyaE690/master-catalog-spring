package kfu.itis.service;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllMastersBySpecialization(Specialization specialization);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User save(User user);
}
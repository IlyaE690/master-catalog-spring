package kfu.itis.service.impl;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import kfu.itis.repository.UserRepository;
import kfu.itis.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("popularMasters")
    public List<User> findAllMasters() {
        return userRepository.findByRoleOrderByRatingDesc(Role.MASTER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllMastersBySpecialization(Specialization specialization) {
        return userRepository.findAllMastersBySpecialization(specialization);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "mastersByName", key = "#query")
    public List<User> findMastersByName(String query) {
        return userRepository.findMastersByName(query);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findMastersBySpecializationAndMinRating(Specialization specialization, Double minRating) {
        return userRepository.findMastersBySpecializationAndMinRating(specialization, minRating);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
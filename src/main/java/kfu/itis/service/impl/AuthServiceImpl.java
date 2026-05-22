package kfu.itis.service.impl;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import kfu.itis.repository.SpecializationRepository;
import kfu.itis.repository.UserRepository;
import kfu.itis.service.AuthService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SpecializationRepository specializationRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, SpecializationRepository specializationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.specializationRepository = specializationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(String username, String email, String password,
                         String firstName, String lastName, String phone, Role role, Long[] specializationIds) {
        if (role == Role.ADMIN) {
            throw new RuntimeException("Невозможно зарегистрировать администратора");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Аккаунт с таким ником уже существует");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Аккаунт с таким email уже существует");
        }

        if (phone != null && !phone.trim().isEmpty() && userRepository.findByPhone(phone).isPresent()) {
            throw new RuntimeException("Аккаунт с таким телефоном уже существует");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone != null && !phone.trim().isEmpty() ? phone : null);
        user.setRole(role);
        user.setEnabled(true);

        if (role == Role.MASTER && specializationIds != null && specializationIds.length > 0) {
            Set<Specialization> specializations = new HashSet<>();
            for (Long id : specializationIds) {
                specializationRepository.findById(id).ifPresent(specializations::add);
            }
            user.setSpecializations(specializations);
        }

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("users_phone_key")) {
                throw new RuntimeException("Аккаунт с таким телефоном уже существует");
            }
            throw new RuntimeException("Ошибка при регистрации: " + e.getMessage());
        }
        return user;
    }
}
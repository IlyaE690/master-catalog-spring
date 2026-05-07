package kfu.itis.service.impl;

import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import kfu.itis.repository.UserRepository;
import kfu.itis.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(String username, String email, String password,
                         String firstName, String lastName, String phone, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Аккаунт с таким ником уже существует");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Аккаунт с таким email уже существует");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setRole(role);
        user.setEnabled(true);

        userRepository.save(user);
        return user;
    }
}

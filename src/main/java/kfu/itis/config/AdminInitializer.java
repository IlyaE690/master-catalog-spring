package kfu.itis.config;

import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;
import kfu.itis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.email:admin@household.ru}")
    private String adminEmail;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Value("${admin.first-name:Админ}")
    private String adminFirstName;

    @Value("${admin.last-name:Системный}")
    private String adminLastName;

    @Value("${admin.phone:+70000000001}")
    private String adminPhone;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFirstName(adminFirstName);
            admin.setLastName(adminLastName);
            admin.setPhone(adminPhone);
            admin.setRole(Role.ADMIN);
            admin.setRating(0.0);
            admin.setEnabled(true);

            userRepository.save(admin);

        }
    }
}
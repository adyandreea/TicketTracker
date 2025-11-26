package com.andreea.ticket_tracker.security.admin;

import com.andreea.ticket_tracker.security.user.User;
import com.andreea.ticket_tracker.security.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.andreea.ticket_tracker.security.user.Role.ADMIN;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            AdminProperties adminProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @Override
    public void run(String... args) throws Exception {

        boolean exists = userRepository.existsByUsername(adminProperties.getUsername());

        if (!exists) {
            User admin = new User();
            admin.setUsername(adminProperties.getUsername());
            admin.setEmail(adminProperties.getEmail());
            admin.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
            admin.setRole(ADMIN);

            userRepository.save(admin);

            System.out.println("ADMIN CREATED");
        } else {
            System.out.println("Admin already exists.");
        }
    }
}

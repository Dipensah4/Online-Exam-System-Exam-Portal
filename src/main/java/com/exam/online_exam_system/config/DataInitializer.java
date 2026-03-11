package com.exam.online_exam_system.config;

import com.exam.online_exam_system.model.Role;
import com.exam.online_exam_system.model.User;
import com.exam.online_exam_system.repository.UserRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      // Find any plaintext passwords and encode them
      List<User> users = userRepository.findAll();
      for (User u : users) {
        if (u.getPassword() != null && !u.getPassword().startsWith("$2a$")) {
          u.setPassword(passwordEncoder.encode(u.getPassword()));
          userRepository.save(u);
          System.out.println("✅ Encoded plaintext password for user: " + u.getUsername());
        }
      }

      if (userRepository.findByUsername("admin") == null) {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        System.out.println("✅ Default admin user created (username: admin, password: admin123)");
      }
    };
  }
}

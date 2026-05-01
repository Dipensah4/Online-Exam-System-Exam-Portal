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
      // ── Fix: Robust password encoding check ────────────────────────────────
      // Avoid re-encoding already hashed passwords (checking for common BCrypt prefixes)
      List<User> users = userRepository.findAll();
      for (User u : users) {
        String pwd = u.getPassword();
        if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
          u.setPassword(passwordEncoder.encode(pwd));
          userRepository.save(u);
          System.out.println("✅ Encoded plaintext password for user: " + u.getUsername());
        }
      }

      // ── Ensure Admin User Exists ───────────────────────────────────────────
      User admin = userRepository.findByUsername("admin");
      boolean forceReset = "true".equalsIgnoreCase(System.getenv("ADMIN_FORCE_RESET"));

      if (admin == null) {
        admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        admin.setRealName("System Administrator");
        userRepository.save(admin);
        System.out.println("✅ Default admin user created (username: admin, password: admin123)");
      } else if (forceReset) {
        // Force reset the password if the environment variable is set
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        System.out.println("⚠️ ADMIN_FORCE_RESET is TRUE: Admin password has been reset to 'admin123'");
      }
    };
  }
}

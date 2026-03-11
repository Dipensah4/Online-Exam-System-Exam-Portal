package com.exam.online_exam_system.service;

import com.exam.online_exam_system.model.User;
import com.exam.online_exam_system.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @SuppressWarnings("null")
  public User save(User user) {
    return Objects.requireNonNull(userRepository.save(user), "Failed to save User");
  }

  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(userRepository.findByUsername(username));
  }

  public boolean usernameExists(String username) {
    return findByUsername(username).isPresent();
  }
}

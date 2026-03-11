package com.exam.online_exam_system.repository;

import com.exam.online_exam_system.model.Student;
import com.exam.online_exam_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
  Student findByEmail(String email);

  Student findByUser(User user);

  java.util.List<Student>
      findByNameContainingIgnoreCaseOrMobileNumberContainingOrUserUsernameContainingIgnoreCase(
          String name, String mobileNumber, String username);
}

package com.exam.online_exam_system.service;

import com.exam.online_exam_system.exception.ResourceNotFoundException;
import com.exam.online_exam_system.model.Student;
import com.exam.online_exam_system.model.Submission;
import com.exam.online_exam_system.model.User;
import com.exam.online_exam_system.repository.StudentAnswerRepository;
import com.exam.online_exam_system.repository.StudentRepository;
import com.exam.online_exam_system.repository.SubmissionRepository;
import com.exam.online_exam_system.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

  @Autowired private StudentRepository studentRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private SubmissionRepository submissionRepository;

  @Autowired private StudentAnswerRepository studentAnswerRepository;

  @SuppressWarnings("null")
  public Student save(Student student) {
    return Objects.requireNonNull(studentRepository.save(student), "Failed to save Student");
  }

  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }

  @SuppressWarnings("null")
  public Student getById(Long id) {
    return studentRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student", id));
  }

  public Optional<Student> findByUser(User user) {
    return Optional.ofNullable(studentRepository.findByUser(user));
  }

  public Student getStudentFromUsername(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      return null;
    }
    return studentRepository.findByUser(user);
  }

  @Transactional
  @SuppressWarnings("null")
  public void deleteStudent(Long id) {
    Student student =
        studentRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student", id));

    // 1. Delete all answers for this student's submissions
    List<Submission> submissions =
        submissionRepository.findByStudentOrderBySubmittedAtDesc(student);
    for (Submission sub : submissions) {
      studentAnswerRepository.deleteBySubmission(sub);
    }

    // 2. Delete submissions
    submissionRepository.deleteByStudent(student);

    // 3. Keep the User object for a moment to delete it last
    User user = student.getUser();

    // 4. Delete the student profile
    studentRepository.delete(student);

    // 5. Delete the associated User account
    if (user != null) {
      userRepository.delete(user);
    }
  }

  public long count() {
    return studentRepository.count();
  }

  public boolean emailExists(String email) {
    return studentRepository.findByEmail(email) != null;
  }

  public List<Student> searchStudents(String query) {
    if (query == null || query.trim().isEmpty()) {
      return studentRepository.findAll();
    }
    return studentRepository
        .findByNameContainingIgnoreCaseOrMobileNumberContainingOrUserUsernameContainingIgnoreCase(
            query, query, query);
  }
}

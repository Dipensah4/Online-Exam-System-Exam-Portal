package com.exam.online_exam_system.repository;

import com.exam.online_exam_system.model.StudentAnswer;
import com.exam.online_exam_system.model.Submission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
  List<StudentAnswer> findBySubmission(Submission submission);

  @org.springframework.transaction.annotation.Transactional
  @org.springframework.data.jpa.repository.Modifying
  void deleteBySubmission(Submission submission);

  @org.springframework.transaction.annotation.Transactional
  @org.springframework.data.jpa.repository.Modifying
  void deleteByQuestion(com.exam.online_exam_system.model.Question question);

  long countByCorrectTrue();
}

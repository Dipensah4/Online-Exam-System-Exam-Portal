package com.exam.online_exam_system.repository;

import com.exam.online_exam_system.model.Student;
import com.exam.online_exam_system.model.Submission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
  List<Submission> findByStudentOrderBySubmittedAtDesc(Student student);

  @org.springframework.transaction.annotation.Transactional
  @org.springframework.data.jpa.repository.Modifying
  void deleteByStudent(Student student);

  long countByStudent(Student student);

  boolean existsByStudentAndExam(Student student, com.exam.online_exam_system.model.Exam exam);

  List<Submission> findByExam(com.exam.online_exam_system.model.Exam exam);

  @org.springframework.transaction.annotation.Transactional
  @org.springframework.data.jpa.repository.Modifying
  void deleteByExam(com.exam.online_exam_system.model.Exam exam);

  @Query(
      "SELECT s FROM Submission s WHERE s.id IN "
          + "(SELECT MAX(s2.id) FROM Submission s2 GROUP BY s2.student.id, s2.exam.id) "
          + "ORDER BY s.percentage DESC, s.submittedAt ASC")
  List<Submission> findTopUniqueSubmissions();

  @Query("SELECT s FROM Submission s ORDER BY s.percentage DESC, s.submittedAt ASC")
  List<Submission> findTopSubmissions();

  long countByExamAndPercentageGreaterThan(
      com.exam.online_exam_system.model.Exam exam, double percentage);

  @Query("SELECT AVG(s.percentage) FROM Submission s WHERE s.exam = :exam")
  Double getAveragePercentageByExam(com.exam.online_exam_system.model.Exam exam);
}

package com.exam.online_exam_system.repository;

import com.exam.online_exam_system.model.Exam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
  @org.springframework.data.jpa.repository.Query("SELECT e FROM Exam e LEFT JOIN FETCH e.questions")
  List<Exam> findAllWithQuestions();

  @org.springframework.data.jpa.repository.Query("SELECT e FROM Exam e LEFT JOIN FETCH e.questions WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))")
  List<Exam> findByTitleContainingIgnoreCaseWithQuestions(@org.springframework.data.repository.query.Param("title") String title);

  @org.springframework.data.jpa.repository.Query("SELECT e FROM Exam e LEFT JOIN FETCH e.questions WHERE e.id = :id")
  java.util.Optional<Exam> findByIdWithQuestions(@org.springframework.data.repository.query.Param("id") Long id);

  List<Exam> findByTitleContainingIgnoreCase(String title);
}

package com.exam.online_exam_system.repository;

import com.exam.online_exam_system.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  List<Question> findByExamId(Long examId);
  List<Question> findByQuestionTextContainingIgnoreCase(String query);
  List<Question> findByExamIdAndQuestionTextContainingIgnoreCase(Long examId, String query);
}

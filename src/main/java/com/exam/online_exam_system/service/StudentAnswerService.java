package com.exam.online_exam_system.service;

import com.exam.online_exam_system.model.StudentAnswer;
import com.exam.online_exam_system.repository.StudentAnswerRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAnswerService {

  @Autowired private StudentAnswerRepository repository;

  public StudentAnswer submitAnswer(StudentAnswer answer) {
    if (answer.getSelectedAnswer() != null
        && answer.getSelectedAnswer().equalsIgnoreCase(answer.getCorrectAnswer())) {
      answer.setCorrect(true);
    } else {
      answer.setCorrect(false);
    }
    return Objects.requireNonNull(repository.save(answer), "Failed to save StudentAnswer");
  }
}

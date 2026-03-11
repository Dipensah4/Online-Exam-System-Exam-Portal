package com.exam.online_exam_system.service;

import com.exam.online_exam_system.exception.ResourceNotFoundException;
import com.exam.online_exam_system.model.Exam;
import com.exam.online_exam_system.model.Submission;
import com.exam.online_exam_system.repository.ExamRepository;
import com.exam.online_exam_system.repository.StudentAnswerRepository;
import com.exam.online_exam_system.repository.SubmissionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamService {

  @Autowired private ExamRepository examRepository;

  @Autowired private SubmissionRepository submissionRepository;

  @Autowired private StudentAnswerRepository studentAnswerRepository;

  public Exam createExam(Exam exam) {
    if (exam == null) {
      throw new IllegalArgumentException("Exam cannot be null");
    }
    return examRepository.save(exam);
  }

  public List<Exam> getAllExams() {
    return examRepository.findAllWithQuestions();
  }

  public Exam getExamById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Exam ID cannot be null");
    }
    Optional<Exam> examOptional = examRepository.findById(id);
    if (examOptional.isPresent()) {
      return examOptional.get();
    } else {
      throw new ResourceNotFoundException("Exam", id);
    }
  }

  public Exam getExamByIdWithQuestions(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Exam ID cannot be null");
    }
    Optional<Exam> examOptional = examRepository.findByIdWithQuestions(id);
    if (examOptional.isPresent()) {
      return examOptional.get();
    } else {
      throw new ResourceNotFoundException("Exam", id);
    }
  }

  public Exam updateExam(
      Long id,
      String title,
      String description,
      int duration,
      int passingPercentage,
      LocalDate examDate) {
    Exam exam = getExamById(id);
    exam.setTitle(title);
    exam.setDescription(description);
    exam.setDuration(duration);
    exam.setPassingPercentage(passingPercentage);
    exam.setExamDate(examDate);
    return examRepository.save(exam);
  }

  @Transactional
  public void deleteExam(Long id) {
    Exam exam = getExamById(id);

    List<Submission> submissions = submissionRepository.findByExam(exam);
    for (Submission sub : submissions) {
      studentAnswerRepository.deleteBySubmission(sub);
    }

    submissionRepository.deleteByExam(exam);
    if (exam != null) {
      examRepository.delete(exam);
    }
  }

  public List<Exam> searchExams(String query) {
    if (query == null || query.trim().isEmpty()) {
      return getAllExams();
    }
    return examRepository.findByTitleContainingIgnoreCaseWithQuestions(query);
  }

  public long count() {
    return examRepository.count();
  }
}

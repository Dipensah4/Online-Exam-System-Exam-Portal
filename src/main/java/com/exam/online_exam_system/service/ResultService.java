package com.exam.online_exam_system.service;

import com.exam.online_exam_system.exception.ResourceNotFoundException;
import com.exam.online_exam_system.model.Exam;
import com.exam.online_exam_system.model.Student;
import com.exam.online_exam_system.model.StudentAnswer;
import com.exam.online_exam_system.model.Submission;
import com.exam.online_exam_system.repository.StudentAnswerRepository;
import com.exam.online_exam_system.repository.SubmissionRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResultService {

  @Autowired private SubmissionRepository submissionRepository;

  @Autowired private StudentAnswerRepository studentAnswerRepository;

  // ── Submission CRUD ─────────────────────────────────────────────────────

  @SuppressWarnings("null")
  public Submission saveSubmission(Submission submission) {
    return Objects.requireNonNull(
        submissionRepository.save(submission), "Failed to save Submission");
  }

  public List<Submission> getAllSubmissions() {
    return submissionRepository.findAll();
  }

  public List<Submission> getResultsForStudent(Student student) {
    return submissionRepository.findByStudentOrderBySubmittedAtDesc(student);
  }

  @SuppressWarnings("null")
  public Submission getSubmissionById(Long id) {
    return submissionRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Submission", id));
  }

  public long countByStudent(Student student) {
    return submissionRepository.countByStudent(student);
  }

  public boolean hasStudentTakenExam(Student student, Exam exam) {
    return submissionRepository.existsByStudentAndExam(student, exam);
  }

  public long count() {
    return submissionRepository.count();
  }

  // ── StudentAnswer CRUD ──────────────────────────────────────────────────

  @SuppressWarnings("null")
  public StudentAnswer saveAnswer(StudentAnswer answer) {
    return Objects.requireNonNull(
        studentAnswerRepository.save(answer), "Failed to save StudentAnswer");
  }

  public List<StudentAnswer> findAnswersBySubmission(Submission submission) {
    return studentAnswerRepository.findBySubmission(submission);
  }

  public int getRankForSubmission(Submission submission) {
    if (submission == null || submission.getExam() == null) return 1;
    // Rank is 1 + number of people with higher percentage in the same exam
    return (int)
            submissionRepository.countByExamAndPercentageGreaterThan(
                submission.getExam(), submission.getPercentage())
        + 1;
  }

  public double getAveragePercentageForExam(Exam exam) {
    if (exam == null) return 0.0;
    Double avg = submissionRepository.getAveragePercentageByExam(exam);
    return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
  }
}

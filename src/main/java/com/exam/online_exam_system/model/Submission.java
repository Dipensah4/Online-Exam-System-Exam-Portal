package com.exam.online_exam_system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "submissions")
public class Submission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "student_id")
  private Student student;

  @ManyToOne
  @JoinColumn(name = "exam_id")
  @NotFound(action = NotFoundAction.IGNORE)
  private Exam exam;

  private int score;

  private int totalQuestions;

  private double percentage;

  private String status; // PASS or FAIL

  private LocalDateTime submittedAt;

  @Column(name = "correct_answers")
  private int correctAnswers;

  @Column(name = "total_marks")
  private double totalMarks;

  @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<StudentAnswer> answers;

  public Long getId() {
    return id;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Exam getExam() {
    return exam;
  }

  public void setExam(Exam exam) {
    this.exam = exam;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getTotalQuestions() {
    return totalQuestions;
  }

  public void setTotalQuestions(int totalQuestions) {
    this.totalQuestions = totalQuestions;
  }

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  public LocalDateTime getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(LocalDateTime submittedAt) {
    this.submittedAt = submittedAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public boolean isPassed() {
    return "PASS".equalsIgnoreCase(status);
  }

  public int getCorrectAnswers() {
    return correctAnswers;
  }

  public void setCorrectAnswers(int correctAnswers) {
    this.correctAnswers = correctAnswers;
  }

  public double getTotalMarks() {
    return totalMarks;
  }

  public void setTotalMarks(double totalMarks) {
    this.totalMarks = totalMarks;
  }

  public List<StudentAnswer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<StudentAnswer> answers) {
    this.answers = answers;
  }
}

package com.exam.online_exam_system.model;

import jakarta.persistence.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String selectedAnswer;

  private String correctAnswer;

  private boolean correct;

  @ManyToOne
  @JoinColumn(name = "question_id")
  @NotFound(action = NotFoundAction.IGNORE)
  private Question question;

  @ManyToOne
  @JoinColumn(name = "submission_id")
  @NotFound(action = NotFoundAction.IGNORE)
  private Submission submission;

  public StudentAnswer() {}

  public Long getId() {
    return id;
  }

  public String getSelectedAnswer() {
    return selectedAnswer;
  }

  public void setSelectedAnswer(String selectedAnswer) {
    this.selectedAnswer = selectedAnswer;
  }

  public String getCorrectAnswer() {
    return correctAnswer;
  }

  public void setCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
  }

  public boolean isCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public Submission getSubmission() {
    return submission;
  }

  public void setSubmission(Submission submission) {
    this.submission = submission;
  }
}

package com.exam.online_exam_system.model;

import jakarta.persistence.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "questions")
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String questionText;

  private String optionA;
  private String optionB;
  private String optionC;
  private String optionD;

  private String correctAnswer;

  private String difficulty = "Easy";

  private int marks = 1;

  @ManyToOne
  @JoinColumn(name = "exam_id")
  @NotFound(action = NotFoundAction.IGNORE)
  @com.fasterxml.jackson.annotation.JsonIgnore
  private Exam exam;

  public Long getId() {
    return id;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public String getOptionA() {
    return optionA;
  }

  public void setOptionA(String optionA) {
    this.optionA = optionA;
  }

  public String getOptionB() {
    return optionB;
  }

  public void setOptionB(String optionB) {
    this.optionB = optionB;
  }

  public String getOptionC() {
    return optionC;
  }

  public void setOptionC(String optionC) {
    this.optionC = optionC;
  }

  public String getOptionD() {
    return optionD;
  }

  public void setOptionD(String optionD) {
    this.optionD = optionD;
  }

  public String getCorrectAnswer() {
    return correctAnswer;
  }

  public void setCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
  }

  public Exam getExam() {
    return exam;
  }

  public void setExam(Exam exam) {
    this.exam = exam;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public int getMarks() {
    return marks;
  }

  public void setMarks(int marks) {
    this.marks = marks;
  }
}

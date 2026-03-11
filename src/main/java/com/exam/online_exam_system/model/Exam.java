package com.exam.online_exam_system.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "exams")
public class Exam {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String description;

  private int duration;

  private int passingPercentage = 33;

  private LocalDate examDate;

  @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Question> questions;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public int getPassingPercentage() {
    return passingPercentage;
  }

  public void setPassingPercentage(int passingPercentage) {
    this.passingPercentage = passingPercentage;
  }

  public LocalDate getExamDate() {
    return examDate;
  }

  public void setExamDate(LocalDate examDate) {
    this.examDate = examDate;
  }
}

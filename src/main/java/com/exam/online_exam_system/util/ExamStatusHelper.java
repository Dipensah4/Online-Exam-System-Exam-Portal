package com.exam.online_exam_system.util;

import com.exam.online_exam_system.model.Exam;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component("examStatusHelper")
public class ExamStatusHelper {

  public static String getStatus(Exam exam) {
    if (exam.getExamDate() == null) {
      return "UNKNOWN";
    }

    LocalDate currentDate = LocalDate.now();

    if (currentDate.isBefore(exam.getExamDate())) {
      return "Upcoming";
    } else if (currentDate.isEqual(exam.getExamDate())) {
      return "Active";
    } else {
      return "Missed";
    }
  }
}

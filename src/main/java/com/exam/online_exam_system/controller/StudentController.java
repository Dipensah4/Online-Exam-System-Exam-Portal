package com.exam.online_exam_system.controller;

import com.exam.online_exam_system.model.*;
import com.exam.online_exam_system.service.*;
import com.exam.online_exam_system.util.ExamStatusHelper;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling Student-facing interactions.
 * Provides endpoints for the student dashboard, exam attempts, profile management, and viewing results.
 */
@Controller
@RequestMapping("/student")
public class StudentController {

  @Autowired private ExamService examService;
  @Autowired private StudentService studentService;
  @Autowired private ResultService resultService;

  /**
   * Helper method to extract the Student entity from the current authentication context.
   *
   * @param auth the Spring Security authentication token
   * @return the associated Student entity, or null if not found
   */
  private Student getStudentFromAuth(Authentication auth) {
    return studentService.getStudentFromUsername(auth.getName());
  }

  /**
   * Renders the main dashboard for the logged-in student.
   * Calculates high-level metrics like available exams and average score.
   *
   * @param auth the authentication context
   * @param model the Spring MVC model
   * @return the view template path for the dashboard
   */
  @GetMapping("/dashboard")
  public String dashboard(Authentication auth, Model model) {
    Student student = getStudentFromAuth(auth);
    long availableExams = examService.count();
    long completedExams = student != null ? resultService.countByStudent(student) : 0;

    double avgScore = 0;
    if (student != null) {
      List<Submission> subs = resultService.getResultsForStudent(student);
      if (!subs.isEmpty()) {
        avgScore = subs.stream().mapToDouble(Submission::getPercentage).average().orElse(0);
        avgScore = Math.round(avgScore * 10.0) / 10.0;
      }
    }

    model.addAttribute("availableExams", availableExams);
    model.addAttribute("completedExams", completedExams);
    model.addAttribute("avgScore", avgScore > 0 ? avgScore : null);
    return "student/student-dashboard";
  }

  // ── AJAX endpoint for live student stats ─────────────────────────────────

  /**
   * Provides real-time statistics for the student dashboard via REST API.
   *
   * @param auth the authentication context
   * @return a ResponseEntity containing various student performance metrics
   */
  @GetMapping("/stats")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> studentStats(Authentication auth) {
    Student student = getStudentFromAuth(auth);
    long available = examService.count();
    long completed = student != null ? resultService.countByStudent(student) : 0;
    double avg = 0;
    double best = 0;
    if (student != null) {
      List<Submission> subs = resultService.getResultsForStudent(student);
      if (!subs.isEmpty()) {
        avg = subs.stream().mapToDouble(Submission::getPercentage).average().orElse(0);
        avg = Math.round(avg * 10.0) / 10.0;
        best = subs.stream().mapToDouble(Submission::getPercentage).max().orElse(0);
        best = Math.round(best * 10.0) / 10.0;
      }
    }
    return ResponseEntity.ok(
        Map.of(
            "availableExams", available,
            "completedExams", completed,
            "avgScore", avg,
            "bestScore", best));
  }

  /**
   * Displays the list of all available exams, tracking which ones the student has already taken.
   *
   * @param auth the authentication context
   * @param model the Spring MVC model
   * @return the view template path for the exam listing
   */
  @GetMapping("/exams")
  public String examList(Authentication auth, Model model) {
    Student student = getStudentFromAuth(auth);
    List<Exam> exams = examService.getAllExams();
    model.addAttribute("exams", exams);

    // Build a set of exam IDs the student has already completed
    if (student != null) {
      Set<Long> completedExamIds =
          resultService.getResultsForStudent(student).stream()
              .filter(sub -> sub.getExam() != null)
              .map(sub -> sub.getExam().getId())
              .collect(Collectors.toSet());
      model.addAttribute("completedExamIds", completedExamIds);
    } else {
      model.addAttribute("completedExamIds", Set.of());
    }
    return "student/exam-list";
  }

  /**
   * Handles the request to start an examination.
   * Performs validation to ensure the exam is active and hasn't been taken already.
   *
   * @param id the unique identifier of the exam
   * @param auth the authentication context
   * @param model the Spring MVC model
   * @return the specific exam taking interface, or a redirect error if invalid
   */
  @GetMapping("/take-exam/{id}")
  public String takeExam(@PathVariable Long id, Authentication auth, Model model) {
    Exam exam = examService.getExamByIdWithQuestions(id);
    Student student = getStudentFromAuth(auth);

    if (student != null && exam != null) {
      String status = ExamStatusHelper.getStatus(exam);
      if ("Upcoming".equals(status)) {
        return "redirect:/student/exams?error=upcoming&date=" + exam.getExamDate();
      } else if ("Missed".equals(status)) {
        return "redirect:/student/exams?error=missed";
      }

      if (resultService.hasStudentTakenExam(student, exam)) {
        return "redirect:/student/exams?error=already_taken";
      }
    }

    model.addAttribute("exam", exam);
    return "student/take-exam";
  }

  /**
   * Processes the exam submission from the student interface.
   * Evaluates answers, calculates the score and percentage, and determines pass/fail status.
   *
   * @param examId the identifier for the exam being submitted
   * @param request the HttpServletRequest containing the raw form answers
   * @param auth the authentication context representing the student
   * @param redirectAttributes used for flash messages
   * @return a redirect instruction to the exam review page
   */
  @PostMapping("/submit-exam/{examId}")
  public String submitExam(
      @PathVariable Long examId,
      HttpServletRequest request,
      Authentication auth,
      org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

    Exam exam = examService.getExamByIdWithQuestions(examId);
    Student student = getStudentFromAuth(auth);

    if (student == null || exam == null) {
      return "redirect:/student/exams";
    }

    // Guard: prevent duplicate submissions
    if (resultService.hasStudentTakenExam(student, exam)) {
      return "redirect:/student/exams?error=already_taken";
    }

    // Create and save submission
    Submission submission = new Submission();
    submission.setExam(exam);
    submission.setStudent(student);
    submission.setSubmittedAt(LocalDateTime.now());

    // Security Guard: Check if exam is for today
    String status = ExamStatusHelper.getStatus(exam);
    if (!"Active".equals(status)) {
      return "redirect:/student/exams?error=missed";
    }

    List<Question> questions = exam.getQuestions();
    List<StudentAnswer> studentAnswers = new ArrayList<>();
    int earnedMarks = 0;
    int totalPossibleMarks = 0;
    int correctCount = 0;

    if (questions != null) {
      for (Question q : questions) {
        String selected = request.getParameter("answer_" + q.getId());

        StudentAnswer answer = new StudentAnswer();
        answer.setQuestion(q);
        answer.setSubmission(submission);
        answer.setCorrectAnswer(q.getCorrectAnswer());

        totalPossibleMarks += q.getMarks();
        if (selected != null && !selected.isEmpty()) {
          answer.setSelectedAnswer(selected.trim());
          String dbCorrectAnswer = q.getCorrectAnswer() != null ? q.getCorrectAnswer().trim() : "";
          boolean isCorrect = selected.trim().equalsIgnoreCase(dbCorrectAnswer);
          answer.setCorrect(isCorrect);
          if (isCorrect) {
            earnedMarks += q.getMarks();
            correctCount++;
          }
        } else {
          answer.setSelectedAnswer("");
          answer.setCorrect(false);
        }
        studentAnswers.add(answer);
      }
    }

    double percentage =
        totalPossibleMarks > 0
            ? Math.round(((double) earnedMarks / totalPossibleMarks) * 1000.0) / 10.0
            : 0.0;

    submission.setScore(earnedMarks);
    submission.setTotalQuestions(questions != null ? questions.size() : 0);
    submission.setTotalMarks((double) totalPossibleMarks);
    submission.setPercentage(percentage);
    submission.setCorrectAnswers(correctCount);
    submission.setAnswers(studentAnswers);

    // Determine status based on passing weight
    if (percentage >= exam.getPassingPercentage()) {
      submission.setStatus("PASS");
    } else {
      submission.setStatus("FAIL");
    }

    Submission saved = resultService.saveSubmission(submission);
    redirectAttributes.addFlashAttribute("submissionSuccess", true);
    return "redirect:/student/review/" + saved.getId();
  }

  /**
   * Renders the profile page for the logged-in student, calculating historical performance.
   *
   * @param auth the authentication context
   * @param model the Spring MVC model
   * @return the view template path for the profile interface
   */
  @GetMapping("/profile")
  public String profile(Authentication auth, Model model) {
    Student student = getStudentFromAuth(auth);
    if (student != null) {
      List<Submission> subs = resultService.getResultsForStudent(student);
      double avg = subs.stream().mapToDouble(Submission::getPercentage).average().orElse(0);
      double best = subs.stream().mapToDouble(Submission::getPercentage).max().orElse(0);
      model.addAttribute("student", student);
      model.addAttribute("completedExams", (long) subs.size());
      model.addAttribute("avgScore", avg > 0 ? Math.round(avg * 10.0) / 10.0 : null);
      model.addAttribute("bestScore", best > 0 ? Math.round(best * 10.0) / 10.0 : null);
    }
    return "student/profile";
  }

  /**
   * Processes a request from the student to update their profile information.
   *
   * @param auth the authentication context
   * @param name the new name
   * @param mobileNumber the new mobile number
   * @return a redirect to the profile page
   */
  @PostMapping("/profile/update")
  public String updateProfile(
      Authentication auth, 
      @RequestParam String name, 
      @RequestParam String mobileNumber,
      org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
    Student student = getStudentFromAuth(auth);
    if (student != null) {
      student.setName(name);
      student.setMobileNumber(mobileNumber);
      studentService.save(student);
      redirectAttributes.addFlashAttribute("successMsg", "Profile updated successfully!");
    } else {
      redirectAttributes.addFlashAttribute("errorMsg", "Student profile not found.");
    }
    return "redirect:/student/profile";
  }

  /**
   * Displays the historical exam results and submissions for the logged-in student.
   *
   * @param auth the authentication context
   * @param model the Spring MVC model
   * @return the view template for historical results
   */
  @GetMapping("/results")
  public String results(Authentication auth, Model model) {
    Student student = getStudentFromAuth(auth);
    if (student != null) {
      model.addAttribute("submissions", resultService.getResultsForStudent(student));
    }
    return "student/results";
  }
}

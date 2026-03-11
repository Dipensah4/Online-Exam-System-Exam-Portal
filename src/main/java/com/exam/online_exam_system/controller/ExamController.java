package com.exam.online_exam_system.controller;

import com.exam.online_exam_system.model.Exam;
import com.exam.online_exam_system.service.ExamService;
import com.exam.online_exam_system.service.QuestionService;
import com.exam.online_exam_system.service.ResultService;
import com.exam.online_exam_system.service.StudentService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling Exam-related administrative actions.
 * Provides endpoints for managing examinations, rendering dashboard analytics,
 * and performing CRUD operations on Exam entities.
 */
@Controller
public class ExamController {

  @Autowired private ExamService examService;

  @Autowired private QuestionService questionService;

  @Autowired private StudentService studentService;

  @Autowired private ResultService resultService;

  @Autowired private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

  @Autowired private com.exam.online_exam_system.service.UserService userService;

  // ── Admin Dashboard ──────────────────────────────────────────────────────

  /**
   * Renders the administrative dashboard view.
   * Populates the model with aggregate metrics across different entity domains.
   *
   * @param model the Spring MVC model
   * @return the view template path for the admin dashboard
   */
  @GetMapping("/admin/dashboard")
  public String dashboard(Model model) {
    model.addAttribute("totalExams", examService.count());
    model.addAttribute("totalStudents", studentService.count());
    model.addAttribute("totalSubmissions", resultService.count());
    model.addAttribute("totalQuestions", questionService.count());
    return "admin/admin-dashboard";
  }

  // ── AJAX endpoint for live dashboard stats ───────────────────────────────

  /**
   * Provides real-time statistics for the admin dashboard via REST API.
   *
   * @return a map containing the current counts of various system entities
   */
  @GetMapping("/api/admin/stats")
  @ResponseBody
  public ResponseEntity<Map<String, Long>> dashboardStats() {
    return ResponseEntity.ok(
        Map.of(
            "totalExams", examService.count(),
            "totalStudents", studentService.count(),
            "totalSubmissions", resultService.count(),
            "totalQuestions", questionService.count()));
  }

  // ── Exam CRUD ────────────────────────────────────────────────────────────

  /**
   * Renders the comprehensive list of all exams for the administrator.
   *
   * @param model the Spring MVC model
   * @return the view template path for the exam list
   */
  @GetMapping("/admin/exams")
  public String examList(Model model) {
    model.addAttribute("exams", examService.getAllExams());
    return "admin/exam";
  }

  /**
   * Redirects the user from the create exam GET endpoint to the exam list.
   *
   * @return a redirect instruction to the exam list endpoint
   */
  @GetMapping("/admin/create-exam")
  public String createExamForm() {
    return "redirect:/admin/exams";
  }

  /**
   * Handles the creation of a new examination record.
   * Formats the request parameters into a new Exam entity and persists it.
   *
   * @param title the exam title
   * @param description an optional description of the exam
   * @param duration the duration of the exam in minutes
   * @param passingPercentage the minimum percentage required to pass
   * @param examDate the scheduled date for the exam
   * @param redirectAttributes attributes to pass flash messages to the view
   * @return a redirect instruction to the questions management endpoint
   */
  @PostMapping("/admin/create-exam")
  public String createExam(
      @RequestParam String title,
      @RequestParam(required = false) String description,
      @RequestParam int duration,
      @RequestParam int passingPercentage,
      @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate examDate,
      org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

    Exam exam = new Exam();
    exam.setTitle(title);
    exam.setDescription(description);
    exam.setDuration(duration);
    exam.setPassingPercentage(passingPercentage);
    exam.setExamDate(examDate);

    examService.createExam(exam);
    redirectAttributes.addFlashAttribute("examCreated", true);
    return "redirect:/admin/questions";
  }

  /**
   * Redirects the user from the edit exam GET endpoint to the exam list.
   *
   * @param id the identifier of the exam to edit
   * @return a redirect instruction to the exam list endpoint
   */
  @GetMapping("/admin/edit-exam")
  public String editExamForm(@RequestParam Long id) {
    return "redirect:/admin/exams";
  }

  /**
   * Executes an update on an existing examination record using the provided parameters.
   *
   * @param id the identifier of the exam being updated
   * @param title the updated exam title
   * @param description the updated exam description
   * @param duration the updated duration limit
   * @param passingPercentage the updated passing criteria
   * @param examDate the updated date of execution
   * @return a redirect instruction back to the exam list
   */
  @PostMapping("/admin/edit-exam")
  public String editExam(
      @RequestParam Long id,
      @RequestParam String title,
      @RequestParam(required = false) String description,
      @RequestParam int duration,
      @RequestParam int passingPercentage,
      @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate examDate) {

    examService.updateExam(id, title, description, duration, passingPercentage, examDate);
    return "redirect:/admin/exams";
  }

  /**
   * Deletes an exam from the system by its identifier.
   *
   * @param id the unique identifier of the exam to delete
   * @return a redirect instruction back to the exam list
   */
  @PostMapping("/admin/delete-exam/{id}")
  public String deleteExam(@PathVariable Long id) {
    examService.deleteExam(id);
    return "redirect:/admin/exams";
  }

  // ── API for Single-Page Management ───────────────────────────────────────

  /**
   * Exposes a search API endpoint for exams.
   * Provides dynamic filtering based on a text query.
   *
   * @param query the optional search string
   * @return a ResponseEntity containing the matched list of exams
   */
  @GetMapping("/api/admin/exams/search")
  @ResponseBody
  public ResponseEntity<List<Exam>> searchExams(@RequestParam(required = false) String query) {
    return ResponseEntity.ok(examService.searchExams(query));
  }

  /**
   * Retrieves specific details for a single exam via API.
   *
   * @param id the unique identifier of the exam
   * @return a ResponseEntity containing the designated Exam object
   */
  @GetMapping("/api/admin/exams/{id}")
  @ResponseBody
  public ResponseEntity<Exam> getExam(@PathVariable Long id) {
    return ResponseEntity.ok(examService.getExamById(id));
  }

  // ── Admin Profile ────────────────────────────────────────────────────────

  /**
   * Renders the administrative profile page.
   *
   * @param auth the authentication context
   * @param model the Spring MVC model
   * @return the view template path for the admin profile
   */
  @GetMapping("/admin/profile")
  public String adminProfile(org.springframework.security.core.Authentication auth, Model model) {
    userService
        .findByUsername(auth.getName())
        .ifPresent(
            admin -> {
              model.addAttribute("admin", admin);
            });
    return "admin/profile";
  }

  /**
   * Processes updates to the administrative profile.
   *
   * @param auth the authentication context
   * @param realName the updated real name
   * @param mobileNumber the updated mobile number
   * @param password the optional new password
   * @return a redirect back to the profile page
   */
  @PostMapping("/admin/profile/update")
  public String updateAdminProfile(
      org.springframework.security.core.Authentication auth,
      @RequestParam String realName,
      @RequestParam String mobileNumber,
      @RequestParam(required = false) String password,
      org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

    userService
        .findByUsername(auth.getName())
        .ifPresentOrElse(
            admin -> {
              admin.setRealName(realName);
              admin.setMobileNumber(mobileNumber);
              if (password != null && !password.trim().isEmpty()) {
                admin.setPassword(passwordEncoder.encode(password));
              }
              userService.save(admin);
              redirectAttributes.addFlashAttribute("successMsg", "Profile updated successfully!");
            },
            () -> {
              redirectAttributes.addFlashAttribute("errorMsg", "Admin profile not found.");
            });

    return "redirect:/admin/profile";
  }
}

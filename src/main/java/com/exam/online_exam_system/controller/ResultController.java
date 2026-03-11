package com.exam.online_exam_system.controller;

import com.exam.online_exam_system.model.StudentAnswer;
import com.exam.online_exam_system.model.Submission;
import com.exam.online_exam_system.service.ResultService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller responsible for handling examination results and submissions.
 * Provides views for both administrators (monitoring all submissions) and students (reviewing personal results).
 */
@Controller
public class ResultController {

  @Autowired private ResultService resultService;

  @Autowired
  private com.exam.online_exam_system.repository.SubmissionRepository submissionRepository;

  /**
   * Renders the administrative view displaying all student submissions across all exams.
   *
   * @param model the Spring MVC model
   * @return the view template path for the total submissions list
   */
  @GetMapping("/admin/submissions")
  public String submissions(Model model) {
    model.addAttribute("submissions", resultService.getAllSubmissions());
    return "admin/submission";
  }

  /**
   * Displays the high-level result summary for a specific submission to a student.
   *
   * @param submissionId the unique identifier of the submission
   * @param model the Spring MVC model
   * @return the view template path for the student result summary
   */
  @GetMapping("/student/result/{submissionId}")
  public String examResult(@PathVariable Long submissionId, Model model) {
    Submission submission = resultService.getSubmissionById(submissionId);
    model.addAttribute("result", submission);
    model.addAttribute("submission", submission);
    return "student/exam-result";
  }

  /**
   * Displays the detailed review of a specific submission, showing correct and incorrect answers.
   *
   * @param submissionId the unique identifier of the submission
   * @param model the Spring MVC model
   * @return the view template path for the detailed exam review
   */
  @GetMapping("/student/review/{submissionId}")
  public String examReview(@PathVariable Long submissionId, Model model) {
    Submission submission = resultService.getSubmissionById(submissionId);
    List<StudentAnswer> answers = resultService.findAnswersBySubmission(submission);
    model.addAttribute("submission", submission);
    model.addAttribute("answers", answers);
    return "student/exam-review";
  }

  /**
   * Provides administrators with a detailed performance review for a specific student's submission,
   * including calculated rankings and historical averages.
   *
   * @param id the unique identifier of the submission
   * @param model the Spring MVC model
   * @return the view template path for the administrative performance review
   */
  @GetMapping("/admin/submission/{id}")
  public String adminSubmissionDetail(@PathVariable Long id, Model model) {
    Submission submission = resultService.getSubmissionById(id);
    List<StudentAnswer> answers = resultService.findAnswersBySubmission(submission);
    model.addAttribute("submission", submission);
    model.addAttribute("answers", answers);
    model.addAttribute("rank", resultService.getRankForSubmission(submission));
    model.addAttribute("avg", resultService.getAveragePercentageForExam(submission.getExam()));
    return "admin/performance";
  }

  /**
   * Exposes a REST API endpoint to retrieve live statistics about a specific submission.
   *
   * @param id the unique identifier of the submission
   * @return a map containing the student's rank, exam average, and total participants
   */
  @GetMapping("/student/result/{id}/live-stats")
  @org.springframework.web.bind.annotation.ResponseBody
  public java.util.Map<String, Object> getLiveStats(@PathVariable Long id) {
    Submission submission = resultService.getSubmissionById(id);
    java.util.Map<String, Object> stats = new java.util.HashMap<>();
    stats.put("rank", resultService.getRankForSubmission(submission));
    stats.put("average", resultService.getAveragePercentageForExam(submission.getExam()));
    stats.put("totalParticipants", submissionRepository.findByExam(submission.getExam()).size());
    return stats;
  }
}

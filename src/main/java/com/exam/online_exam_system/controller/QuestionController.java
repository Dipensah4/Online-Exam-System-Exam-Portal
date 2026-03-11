package com.exam.online_exam_system.controller;

import com.exam.online_exam_system.model.Exam;
import com.exam.online_exam_system.model.Question;
import com.exam.online_exam_system.service.ExamService;
import com.exam.online_exam_system.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for Question logic and repository management.
 * Provides endpoints for creating, editing, importing, and deleting exam questions.
 */
@Controller
public class QuestionController {

  @Autowired private QuestionService questionService;

  @Autowired private ExamService examService;

  /**
   * Renders the administrative view for managing questions.
   *
   * @param model the Spring MVC model
   * @return the view template path for the questions list
   */
  @GetMapping("/admin/questions")
  public String questions(Model model) {
    model.addAttribute("exams", examService.getAllExams());
    return "admin/question";
  }

  /**
   * Retrieves a list of questions associated with a specific exam via REST API.
   *
   * @param examId the unique identifier of the exam (-1 to fetch all questions)
   * @return a list of Question objects serialized as JSON
   */
  @GetMapping("/admin/api/questions/exam/{examId}")
  @ResponseBody
  public java.util.List<Question> getQuestionsByExam(@PathVariable Long examId) {
    if (examId == -1) {
      return questionService.getAllQuestions();
    }
    return questionService.getQuestionsByExam(examId);
  }

  /**
   * Searches for questions based on a query string and optional exam filter.
   *
   * @param examId the ID of the exam to filter by (-1 for all exams)
   * @param query the search term
   * @return a list of matching Question objects
   */
  @GetMapping("/admin/api/questions/search")
  @ResponseBody
  public java.util.List<Question> searchQuestions(
      @RequestParam(required = false, defaultValue = "-1") Long examId,
      @RequestParam("query") String query) {
    return questionService.searchQuestionsByExam(examId, query);
  }

  /**
   * Deletes a question from the repository via REST API.
   *
   * @param id the unique identifier of the question to delete
   * @return an empty ResponseEntity indicating success
   */
  @DeleteMapping("/admin/api/questions/{id}")
  @ResponseBody
  public org.springframework.http.ResponseEntity<Void> deleteQuestionApi(@PathVariable Long id) {
    questionService.deleteQuestion(id);
    return org.springframework.http.ResponseEntity.ok().build();
  }

  /**
   * Handles the bulk import of questions from an uploaded Excel file.
   *
   * @param file the MultipartFile representing the uploaded Excel document
   * @param examId the intended exam to associate the newly imported questions with
   * @param redirectAttributes used for flash messaging on success or failure
   * @return a redirect instruction back to the questions view
   */
  @PostMapping("/admin/import-questions")
  public String importQuestions(
      @RequestParam("file") MultipartFile file,
      @RequestParam("examId") Long examId,
      RedirectAttributes redirectAttributes) {
    try {
      Exam exam = examService.getExamById(examId);
      questionService.importQuestionsFromExcel(file, exam);
      redirectAttributes.addFlashAttribute(
          "successMsg", "Questions imported successfully from Excel!");
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
          "errorMsg", "Failed to import questions: " + e.getMessage());
    }
    return "redirect:/admin/questions?examId=" + examId;
  }

  /**
   * Handles the manual creation of a new exam question.
   * Automatically calculates point value based on the selected difficulty.
   *
   * @param examId the ID of the exam the question belongs to
   * @param questionText the core question content
   * @param optionA first multiple-choice option
   * @param optionB second multiple-choice option
   * @param optionC optional third multiple-choice option
   * @param optionD optional fourth multiple-choice option
   * @param correctAnswer the exact string matching the correct option
   * @param difficulty the relative difficulty factor
   * @return a redirect instruction back to the questions view
   */
  @PostMapping("/admin/add-question")
  public String addQuestion(
      @RequestParam Long examId,
      @RequestParam String questionText,
      @RequestParam String optionA,
      @RequestParam String optionB,
      @RequestParam(required = false) String optionC,
      @RequestParam(required = false) String optionD,
      @RequestParam String correctAnswer,
      @RequestParam(required = false, defaultValue = "Easy") String difficulty) {

    Exam exam = examService.getExamById(examId);

    Question question = new Question();
    question.setExam(exam);
    question.setQuestionText(questionText);
    question.setOptionA(optionA);
    question.setOptionB(optionB);
    question.setOptionC(optionC);
    question.setOptionD(optionD);
    question.setCorrectAnswer(correctAnswer);
    question.setDifficulty(difficulty);

    int calculatedMarks = 1;
    if ("Medium".equalsIgnoreCase(difficulty)) calculatedMarks = 3;
    else if ("Hard".equalsIgnoreCase(difficulty)) calculatedMarks = 5;
    question.setMarks(calculatedMarks);

    questionService.addQuestion(question);
    return "redirect:/admin/questions?examId=" + examId;
  }

  /**
   * Renders the administrative view to edit an existing question.
   *
   * @param id the unique identifier of the question being edited
   * @param model the Spring MVC model
   * @return the view template path for the question edit form
   */
  @GetMapping("/admin/edit-question/{id}")
  public String editQuestionForm(@PathVariable Long id, Model model) {
    model.addAttribute("question", questionService.getQuestionById(id));
    model.addAttribute("exams", examService.getAllExams());
    return "admin/edit-question";
  }

  /**
   * Processes the form submission to update an existing question.
   * Also recalculates and updates the point value based on the designated difficulty.
   *
   * @param id the ID of the question being updated
   * @param examId the updated associated exam
   * @param questionText the updated text content
   * @param optionA updated first option
   * @param optionB updated second option
   * @param optionC updated third option
   * @param optionD updated fourth option
   * @param correctAnswer updated correct answer key
   * @param difficulty the updated relative difficulty level
   * @return a redirect instruction back to the questions view
   */
  @PostMapping("/admin/edit-question/{id}")
  public String editQuestion(
      @PathVariable Long id,
      @RequestParam Long examId,
      @RequestParam String questionText,
      @RequestParam String optionA,
      @RequestParam String optionB,
      @RequestParam(required = false) String optionC,
      @RequestParam(required = false) String optionD,
      @RequestParam String correctAnswer,
      @RequestParam(required = false, defaultValue = "Easy") String difficulty) {

    Exam exam = examService.getExamById(examId);
    Question question = new Question();
    question.setExam(exam);
    question.setQuestionText(questionText);
    question.setOptionA(optionA);
    question.setOptionB(optionB);
    question.setOptionC(optionC);
    question.setOptionD(optionD);
    question.setCorrectAnswer(correctAnswer);
    question.setDifficulty(difficulty);

    int calculatedMarks = 1;
    if ("Medium".equalsIgnoreCase(difficulty)) calculatedMarks = 3;
    else if ("Hard".equalsIgnoreCase(difficulty)) calculatedMarks = 5;
    question.setMarks(calculatedMarks);

    questionService.updateQuestion(id, question);
    return "redirect:/admin/questions?examId=" + examId;
  }

  /**
   * Processes the deletion of an existing question through a traditional form submission endpoint.
   *
   * @param id the unique identifier of the question to delete
   * @return a redirect instruction back to the questions view
   */
  @PostMapping("/admin/delete-question/{id}")
  public String deleteQuestion(@PathVariable Long id) {
    questionService.deleteQuestion(id);
    return "redirect:/admin/questions";
  }
}

package com.exam.online_exam_system.controller;

import com.exam.online_exam_system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminStudentController {

  @Autowired private StudentService studentService;

  @GetMapping("/admin/students")
  public String studentList(Model model) {
    model.addAttribute("students", studentService.getAllStudents());
    return "admin/student";
  }

  @GetMapping("/admin/students/search")
  @ResponseBody
  public java.util.List<com.exam.online_exam_system.model.Student> searchStudents(
      @RequestParam("query") String query) {
    return studentService.searchStudents(query);
  }

  @PostMapping("/admin/delete-student/{id}")
  public String deleteStudent(@PathVariable Long id) {
    studentService.deleteStudent(id);
    return "redirect:/admin/students";
  }
}

package com.exam.online_exam_system.controller;

import com.exam.online_exam_system.dto.RegisterRequest;
import com.exam.online_exam_system.model.Role;
import com.exam.online_exam_system.model.Student;
import com.exam.online_exam_system.model.User;
import com.exam.online_exam_system.service.StudentService;
import com.exam.online_exam_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired private UserService userService;

  @Autowired private StudentService studentService;

  @Autowired private PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  @Transactional
  public String register(@Valid RegisterRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

    // ── Validation errors from annotations ────────────────────────────────
    if (bindingResult.hasErrors()) {
      model.addAttribute("error", bindingResult.getFieldErrors().get(0).getDefaultMessage());
      return "register";
    }

    // ── Check passwords match ─────────────────────────────────────────────
    if (!request.getPassword().equals(request.getConfirmPassword())) {
      model.addAttribute("error", "Passwords do not match.");
      return "register";
    }

    // ── Check username is not taken ───────────────────────────────────────
    if (userService.usernameExists(request.getUsername())) {
      model.addAttribute("error", "Username '" + request.getUsername() + "' is already taken.");
      return "register";
    }

    // ── Check email is not taken ──────────────────────────────────────────
    if (studentService.emailExists(request.getEmail())) {
      model.addAttribute("error", "Email '" + request.getEmail() + "' is already registered.");
      return "register";
    }

    // ── Create User ──────────────────────────────────────────────────────
    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.STUDENT);
    User savedUser = userService.save(user);

    // ── Create Student profile ───────────────────────────────────────────
    Student student = new Student();
    student.setName(request.getName());
    student.setEmail(request.getEmail());
    student.setMobileNumber(request.getMobileNumber());
    student.setUser(savedUser);
    studentService.save(student);

    redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
    return "redirect:/login?registered";
  }

}

package com.exam.online_exam_system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

  // ── Home page ─────────────────────────────────────────────────────────────
  @GetMapping("/")
  public String index() {
    return "index";
  }

  // ── Login page (GET) — Spring Security handles POST /login automatically ──
  @GetMapping("/login")
  public String login() {
    return "login";
  }

  // ── Register page (GET) ───────────────────────────────────────────────────
  @GetMapping("/register")
  public String register() {
    return "register";
  }

  // ── /dashboard — redirect to correct dashboard based on role ─────────────
  // Called by Spring Security defaultSuccessUrl("/dashboard") after login.
  // ROLE_ADMIN  → /admin/dashboard
  // ROLE_STUDENT → /student/dashboard
  @GetMapping("/dashboard")
  public String dashboard(Authentication authentication) {

    if (authentication == null) {
      return "redirect:/login";
    }

    if (authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return "redirect:/admin/dashboard";
    }

    return "redirect:/student/dashboard";
  }
}

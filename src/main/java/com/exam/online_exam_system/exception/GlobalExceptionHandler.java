package com.exam.online_exam_system.exception;

import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleNotFound(ResourceNotFoundException ex, Model model) {
    model.addAttribute("errorTitle", "Not Found");
    model.addAttribute("errorMessage", ex.getMessage());
    model.addAttribute("errorCode", 404);
    return "error";
  }

  @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
  public String handleValidation(Exception ex, Model model) {
    model.addAttribute("errorTitle", "Validation Error");
    model.addAttribute("errorMessage", "Please check your input and try again.");
    model.addAttribute("errorCode", 400);
    return "error";
  }

  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  public String handleAccessDenied(Exception ex, Model model) {
    model.addAttribute("errorTitle", "Access Denied");
    model.addAttribute("errorMessage", "You do not have permission to access this resource.");
    model.addAttribute("errorCode", 403);
    return "error";
  }

  @ExceptionHandler(Exception.class)
  public String handleGeneric(Exception ex, Model model) {
    ex.printStackTrace();
    model.addAttribute("errorTitle", "Something Went Wrong");
    model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
    model.addAttribute("errorCode", 500);
    return "error";
  }
}

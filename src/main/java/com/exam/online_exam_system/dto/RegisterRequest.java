package com.exam.online_exam_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

  @NotBlank(message = "Full name is required")
  private String name;

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Please enter a valid email address")
  private String email;

  @NotBlank(message = "Mobile number is required")
  private String mobileNumber;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

  @NotBlank(message = "Please confirm your password")
  private String confirmPassword;

  // ── Getters & Setters ────────────────────────────────────────────────────

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
}

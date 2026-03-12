package com.exam.online_exam_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/",
                        "/login",
                        "/register",
                        "/user/register",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/image/**",
                        "/actuator/health",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/error")
                    .permitAll()
                    .requestMatchers("/profile/**")
                    .authenticated()
                    .requestMatchers("/admin/**", "/api/admin/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/student/**", "/api/student/**")
                    .hasRole("STUDENT")
                    .anyRequest()
                    .authenticated())
        .formLogin(
            form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .failureUrl("/login?error")
                    .permitAll())
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll());

    return http.build();
  }
}

package com.exam.online_exam_system.controller;

import com.exam.online_exam_system.model.User;
import com.exam.online_exam_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("photo") MultipartFile file, 
                               Authentication auth, 
                               RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Please select a file to upload.");
            return getProfileRedirect(auth);
        }

        try {
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            user.setProfilePhoto(file.getBytes());
            user.setProfilePhotoType(file.getContentType());
            userService.save(user);
            
            redirectAttributes.addFlashAttribute("successMsg", "Profile photo updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "Failed to upload photo: " + e.getMessage());
        }

        return getProfileRedirect(auth);
    }

    @SuppressWarnings("null")
    @GetMapping("/photo/{username}")
    @ResponseBody
    public ResponseEntity<byte[]> getPhoto(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> {
                    if (user.getProfilePhoto() == null || user.getProfilePhotoType() == null) {
                        return ResponseEntity.notFound().<byte[]>build();
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(user.getProfilePhotoType()))
                            .body(user.getProfilePhoto());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private String getProfileRedirect(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin ? "redirect:/admin/profile" : "redirect:/student/profile";
    }
}

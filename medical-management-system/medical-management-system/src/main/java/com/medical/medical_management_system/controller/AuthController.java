package com.medical.medical_management_system.controller;

import com.medical.medical_management_system.entity.User;
import com.medical.medical_management_system.service.AuthService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes) {

        try {
            // Public registration can create PATIENT accounts only.
            user.setRole("PATIENT");

            authService.register(user);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Registration successful! Please log in."
            );

            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/register";
        }
    }
}
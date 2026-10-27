package com.medical.medical_management_system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isDoctor = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));

        boolean isPatient = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }

        if (isDoctor) {
            return "redirect:/doctor/dashboard";
        }

        if (isPatient) {
            return "redirect:/patient/dashboard";
        }

        return "redirect:/login?error";
    }
}
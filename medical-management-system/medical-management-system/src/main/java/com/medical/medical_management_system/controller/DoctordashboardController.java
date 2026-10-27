package com.medical.medical_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctordashboardController {

@GetMapping("/doctor/dashboard")
public String doctorDashboard() {
    return "doctor/dashboard";
}

}
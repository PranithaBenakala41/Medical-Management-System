package com.medical.medical_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientDashboardController {

@GetMapping("/patients/dashboard")
public String patientDashboard() {
    return "patients/dashboard";
}

}
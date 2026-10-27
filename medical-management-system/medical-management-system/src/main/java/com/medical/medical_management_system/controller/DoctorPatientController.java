package com.medical.medical_management_system.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.service.PatientService;

@Controller
@RequestMapping("/doctor")
public class DoctorPatientController {

private final PatientService patientService;

public DoctorPatientController(PatientService patientService) {
    this.patientService = patientService;
}

@GetMapping("/patients")
public String viewPatients(Model model) {

    List<Patient> patients = patientService.getAllPatients();

    model.addAttribute("patients", patients);

    return "doctor/patients";
}

}
package com.medical.medical_management_system.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.medical.medical_management_system.entity.Appointment;
import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.repository.PatientRepository;
import com.medical.medical_management_system.service.AppointmentService;
import com.medical.medical_management_system.service.PatientService;

@Controller
@RequestMapping("/patients")
public class PatientController {

private final PatientService patientService;
private final AppointmentService appointmentService;
private final PatientRepository patientRepository;

public PatientController(
        PatientService patientService,
        AppointmentService appointmentService,
        PatientRepository patientRepository) {

    this.patientService = patientService;
    this.appointmentService = appointmentService;
    this.patientRepository = patientRepository;
}

// Display all patients for admin management
@GetMapping
public String listPatients(Model model) {

    model.addAttribute(
            "patients",
            patientService.getAllPatients()
    );

    return "patients/list";
}

// Display form to add a patient
@GetMapping("/new")
public String showPatientForm(Model model) {

    model.addAttribute("patient", new Patient());

    return "patients/form";
}

// Save a new or edited patient
@PostMapping("/save")
public String savePatient(
        @ModelAttribute("patient") Patient patient) {

    patientService.savePatient(patient);

    return "redirect:/patients";
}

// Display the edit form
@GetMapping("/edit/{id}")
public String editPatient(
        @PathVariable Long id,
        Model model) {

    Patient patient = patientService.getPatientById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Invalid patient ID: " + id
                    ));

    model.addAttribute("patient", patient);

    return "patients/form";
}

// Delete a patient using POST
@PostMapping("/delete/{id}")
public String deletePatient(@PathVariable Long id) {

    patientService.deletePatient(id);

    return "redirect:/patients";
}

// Display appointments belonging to the logged-in patient
@GetMapping("/appointments")
public String myAppointments(
        Principal principal,
        Model model) {

    if (principal == null) {
        return "redirect:/login";
    }

    String email = principal.getName();

    Patient patient = patientRepository.findByEmail(email)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Patient profile not found for: " + email
                    ));

    List<Appointment> appointments =
            appointmentService.getAppointmentsByPatientId(
                    patient.getId()
            );

    model.addAttribute("appointments", appointments);

    return "appointments/list";
}

}
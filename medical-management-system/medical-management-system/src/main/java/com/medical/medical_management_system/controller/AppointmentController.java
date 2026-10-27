package com.medical.medical_management_system.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.medical.medical_management_system.entity.Appointment;
import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.repository.PatientRepository;
import com.medical.medical_management_system.service.AppointmentService;
import com.medical.medical_management_system.service.DoctorService;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

private final AppointmentService appointmentService;
private final DoctorService doctorService;
private final PatientRepository patientRepository;

public AppointmentController(
        AppointmentService appointmentService,
        DoctorService doctorService,
        PatientRepository patientRepository) {

    this.appointmentService = appointmentService;
    this.doctorService = doctorService;
    this.patientRepository = patientRepository;
}

// Display all appointments for authorized appointment management
@GetMapping
public String listAppointments(Model model) {

    model.addAttribute(
            "appointments",
            appointmentService.getAllAppointments()
    );

    return "appointments/list";
}

// Display the appointment booking form
@GetMapping("/new")
public String showAppointmentForm(Model model) {

    model.addAttribute("appointment", new Appointment());
    model.addAttribute("doctors", doctorService.getAllDoctors());

    return "appointments/form";
}

// Save an appointment and link it to the logged-in patient
@PostMapping("/save")
public String saveAppointment(
        @ModelAttribute("appointment") Appointment appointment,
        Principal principal,
        Model model) {

    if (principal == null) {
        return "redirect:/login";
    }

    String email = principal.getName();

    Patient patient = patientRepository.findByEmail(email)
            .orElse(null);

    if (patient == null) {
        model.addAttribute(
                "errorMessage",
                "Patient profile not found. Please contact the administrator."
        );

        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.getAllDoctors());

        return "appointments/form";
    }

    // Set the patient using the authenticated account.
    // Do not trust a patient ID submitted by the browser.
    appointment.setPatient(patient);
    appointment.setPatientName(patient.getName());

    appointmentService.saveAppointment(appointment);

    return "redirect:/patients/appointments";
}

// Delete an appointment
@GetMapping("/delete/{id}")
public String deleteAppointment(@PathVariable Long id) {

    appointmentService.deleteAppointment(id);

    return "redirect:/appointments";
}

}
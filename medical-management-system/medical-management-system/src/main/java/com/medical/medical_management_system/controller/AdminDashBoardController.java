package com.medical.medical_management_system.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.medical.medical_management_system.repository.AppointmentRepository;
import com.medical.medical_management_system.repository.DoctorRepository;
import com.medical.medical_management_system.repository.PatientRepository;

@Controller
public class AdminDashBoardController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public AdminDashBoardController(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {

        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {

        long totalDoctors = doctorRepository.count();
        long totalPatients = patientRepository.count();
        long todayAppointments =
                appointmentRepository.countByAppointmentDate(LocalDate.now());

        model.addAttribute("totalDoctors", totalDoctors);
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("todayAppointments", todayAppointments);

        return "admin/dashboard";
    }
}
package com.medical.medical_management_system.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.medical.medical_management_system.entity.Doctor;
import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.entity.Prescription;
import com.medical.medical_management_system.repository.DoctorRepository;
import com.medical.medical_management_system.repository.PatientRepository;
import com.medical.medical_management_system.repository.PrescriptionRepository;

@Controller
@RequestMapping("/doctor/prescriptions")
public class DoctorPrescriptionController {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public DoctorPrescriptionController(
            PrescriptionRepository prescriptionRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // Open prescription page
    @GetMapping
    public String viewPrescriptions(
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Doctor doctor = doctorRepository
                .findByEmail(principal.getName())
                .orElse(null);

        if (doctor == null) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Doctor profile not found for this login email.");
            return "redirect:/doctor/dashboard";
        }

        List<Patient> patients = patientRepository.findAll();

        List<Prescription> prescriptions =
                prescriptionRepository.findByDoctor(doctor);

        model.addAttribute("patients", patients);
        model.addAttribute("prescriptions", prescriptions);

        return "doctor/prescriptions";
    }

    // Save prescription
    @PostMapping("/save")
    public String savePrescription(
            Principal principal,
            @RequestParam("patientId") Long patientId,
            @RequestParam("medicines") String medicines,
            @RequestParam(value = "instructions", required = false)
                    String instructions,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Doctor doctor = doctorRepository
                    .findByEmail(principal.getName())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Doctor profile not found."));

            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Selected patient was not found."));

            if (medicines == null || medicines.isBlank()) {
                throw new IllegalArgumentException(
                        "Please enter medicines.");
            }

            Prescription prescription = new Prescription();
            prescription.setDoctor(doctor);
            prescription.setPatient(patient);
            prescription.setMedicines(medicines.trim());
            prescription.setInstructions(
                    instructions == null ? "" : instructions.trim());
            prescription.setPrescriptionDate(LocalDate.now());

            prescriptionRepository.save(prescription);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Prescription saved successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Unable to save prescription: " + e.getMessage());
        }

        return "redirect:/doctor/prescriptions";
    }
}
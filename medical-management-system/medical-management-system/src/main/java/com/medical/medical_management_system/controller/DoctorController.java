package com.medical.medical_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.medical.medical_management_system.entity.Doctor;
import com.medical.medical_management_system.service.AuthService;
import com.medical.medical_management_system.service.DoctorService;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

private final DoctorService doctorService;
private final AuthService authService;

public DoctorController(
        DoctorService doctorService,
        AuthService authService) {
    this.doctorService = doctorService;
    this.authService = authService;
}

// Display all doctors
@GetMapping
public String listDoctors(Model model) {
    model.addAttribute("doctors", doctorService.getAllDoctors());
    return "doctor/list";
}

// Open Add Doctor form
@GetMapping("/new")
public String showDoctorForm(Model model) {
    model.addAttribute("doctor", new Doctor());
    return "doctor/form";
}

// Save new doctor or update existing doctor
@PostMapping("/save")
public String saveDoctor(
        @ModelAttribute("doctor") Doctor doctor,
        @RequestParam(required = false) String password,
        RedirectAttributes redirectAttributes) {

    System.out.println("========== SAVE DOCTOR REQUEST ==========");
    System.out.println("Doctor name: " + doctor.getName());
    System.out.println("Doctor email: " + doctor.getEmail());
    System.out.println("Doctor ID: " + doctor.getId());
    System.out.println("Password received: "
            + (password != null && !password.isBlank()));

    try {
        if (doctor.getId() == null) {

            // Create doctor profile and login account
            authService.createDoctorWithAccount(doctor, password);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Doctor and login account created successfully!");

        } else {

            // Update existing doctor
            Doctor existingDoctor = doctorService
                    .getDoctorById(doctor.getId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Doctor not found."));

            existingDoctor.setName(doctor.getName());
            existingDoctor.setSpecialization(
                    doctor.getSpecialization());
            existingDoctor.setPhone(doctor.getPhone());
            existingDoctor.setEmail(doctor.getEmail());
            existingDoctor.setAvailableDays(
                    doctor.getAvailableDays());

            doctorService.saveDoctor(existingDoctor);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Doctor details updated successfully!");
        }

        System.out.println("Doctor save operation completed.");
        System.out.println("=========================================");

        return "redirect:/doctors";

    } catch (IllegalArgumentException e) {

        e.printStackTrace();

        redirectAttributes.addFlashAttribute(
                "errorMessage",
                e.getMessage());

        if (doctor.getId() == null) {
            return "redirect:/doctors/new";
        }

        return "redirect:/doctors/edit/" + doctor.getId();

    } catch (Exception e) {

        e.printStackTrace();

        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Unable to save doctor: " + e.getMessage());

        if (doctor.getId() == null) {
            return "redirect:/doctors/new";
        }

        return "redirect:/doctors/edit/" + doctor.getId();
    }
}

// Open Edit Doctor form
@GetMapping("/edit/{id}")
public String editDoctor(
        @PathVariable Long id,
        Model model,
        RedirectAttributes redirectAttributes) {

    try {
        Doctor doctor = doctorService
                .getDoctorById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Doctor not found."));

        model.addAttribute("doctor", doctor);

        return "doctor/form";

    } catch (Exception e) {

        e.printStackTrace();

        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Unable to open doctor details: " + e.getMessage());

        return "redirect:/doctors";
    }
}

// Delete doctor
@PostMapping("/delete/{id}")
public String deleteDoctor(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    try {
        doctorService.deleteDoctor(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Doctor deleted successfully.");

    } catch (Exception e) {

        e.printStackTrace();

        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Unable to delete doctor: " + e.getMessage());
    }

    return "redirect:/doctors";
}

}
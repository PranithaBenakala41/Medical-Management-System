package com.medical.medical_management_system.controller;

import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.entity.User;
import com.medical.medical_management_system.repository.PatientRepository;
import com.medical.medical_management_system.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patients")
public class PatientProfileController {

private final PatientRepository patientRepository;
private final UserRepository userRepository;

public PatientProfileController(
        PatientRepository patientRepository,
        UserRepository userRepository) {
    this.patientRepository = patientRepository;
    this.userRepository = userRepository;
}

private Patient getLoggedInPatient(Authentication authentication) {

    if (authentication == null
            || !authentication.isAuthenticated()
            || "anonymousUser".equals(authentication.getPrincipal())) {
        throw new IllegalStateException("Please log in first.");
    }

    User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() ->
                    new IllegalArgumentException("Logged-in user not found"));

    return patientRepository.findByUser(user)
            .orElseGet(() -> {
                Patient patient = new Patient();
                patient.setUser(user);
                patient.setName(user.getName());
                patient.setEmail(user.getEmail());
                return patientRepository.save(patient);
            });
}

@GetMapping("/profile")
public String viewProfile(
        Authentication authentication,
        Model model) {

    Patient patient = getLoggedInPatient(authentication);

    model.addAttribute("patient", patient);

    return "patients/profile";
}

@PostMapping("/profile/update")
public String updateProfile(
        Authentication authentication,
        @ModelAttribute("patient") Patient formPatient,
        RedirectAttributes redirectAttributes) {

    Patient patient = getLoggedInPatient(authentication);

    // Update only the permitted fields.
    patient.setAge(formPatient.getAge());
    patient.setGender(formPatient.getGender());
    patient.setPhone(formPatient.getPhone());
    patient.setAddress(formPatient.getAddress());

    patientRepository.save(patient);

    redirectAttributes.addFlashAttribute(
            "successMessage", "Your profile has been updated successfully!");

    return "redirect:/patients/profile";
}

}
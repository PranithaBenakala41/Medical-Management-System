package com.medical.medical_management_system.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.medical.medical_management_system.entity.Bill;
import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.entity.User;
import com.medical.medical_management_system.repository.BillRepository;
import com.medical.medical_management_system.repository.PatientRepository;
import com.medical.medical_management_system.repository.UserRepository;

@Controller
@RequestMapping("/patients/bills")
public class BillController {

private final BillRepository billRepository;
private final PatientRepository patientRepository;
private final UserRepository userRepository;

public BillController(
        BillRepository billRepository,
        PatientRepository patientRepository,
        UserRepository userRepository) {
    this.billRepository = billRepository;
    this.patientRepository = patientRepository;
    this.userRepository = userRepository;
}

@GetMapping
public String viewBills(Authentication authentication, Model model) {

    User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() ->
                    new IllegalArgumentException("Logged-in user not found"));

    Patient patient = patientRepository.findByUser(user)
            .orElseGet(() -> {
                Patient newPatient = new Patient();
                newPatient.setUser(user);
                newPatient.setName(user.getName());
                newPatient.setEmail(user.getEmail());
                return patientRepository.save(newPatient);
            });

    List<Bill> bills = billRepository.findByPatient(patient);

    model.addAttribute("bills", bills);

    return "patients/bills";
}

}
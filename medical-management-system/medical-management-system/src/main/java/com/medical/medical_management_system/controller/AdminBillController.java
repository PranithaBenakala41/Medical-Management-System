package com.medical.medical_management_system.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.medical.medical_management_system.entity.Bill;
import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.repository.BillRepository;
import com.medical.medical_management_system.repository.PatientRepository;

@Controller
@RequestMapping("/admin/bills")
public class AdminBillController {

private final BillRepository billRepository;
private final PatientRepository patientRepository;

public AdminBillController(
        BillRepository billRepository,
        PatientRepository patientRepository) {
    this.billRepository = billRepository;
    this.patientRepository = patientRepository;
}

@GetMapping
public String listBills(Model model) {
    List<Bill> bills = billRepository.findAll();
    model.addAttribute("bills", bills);
    return "admin/bills";
}

@GetMapping("/new")
public String showBillForm(Model model) {
    model.addAttribute("bill", new Bill());
    model.addAttribute("patients", patientRepository.findAll());
    return "admin/bill-form";
}

@PostMapping("/save")
public String saveBill(
        @ModelAttribute("bill") Bill billForm,
        RedirectAttributes redirectAttributes) {

    if (billForm.getPatient() == null
            || billForm.getPatient().getId() == null) {
        redirectAttributes.addFlashAttribute(
                "errorMessage", "Please select a patient.");
        return "redirect:/admin/bills/new";
    }

    Patient patient = patientRepository.findById(
            billForm.getPatient().getId())
            .orElseThrow(() ->
                    new IllegalArgumentException("Patient not found"));

    billForm.setPatient(patient);

    if (billForm.getBillDate() == null) {
        billForm.setBillDate(LocalDate.now());
    }

    if (billForm.getStatus() == null
            || billForm.getStatus().isBlank()) {
        billForm.setStatus("Pending");
    }

    billRepository.save(billForm);

    redirectAttributes.addFlashAttribute(
            "successMessage", "Bill saved successfully!");

    return "redirect:/admin/bills";
}

@PostMapping("/delete/{id}")
public String deleteBill(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    if (billRepository.existsById(id)) {
        billRepository.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Bill deleted successfully!");
    }

    return "redirect:/admin/bills";
}

}
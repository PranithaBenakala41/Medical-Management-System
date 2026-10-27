package com.medical.medical_management_system.controller;

import com.medical.medical_management_system.entity.Department;
import com.medical.medical_management_system.repository.DepartmentRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Display all departments
    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin/departments";
    }

    // Display form to add department
    @GetMapping("/new")
    public String showDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "admin/department-form";
    }

    // Save department
    @PostMapping("/save")
    public String saveDepartment(
            @ModelAttribute("department") Department department,
            RedirectAttributes redirectAttributes) {

        departmentRepository.save(department);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Department saved successfully!");

        return "redirect:/admin/departments";
    }

    // Delete department
    @PostMapping("/delete/{id}")
    public String deleteDepartment(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Department deleted successfully!");
        }

        return "redirect:/admin/departments";
    }
}
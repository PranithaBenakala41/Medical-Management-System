package com.medical.medical_management_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.entity.Prescription;
import com.medical.medical_management_system.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPatientPrescriptions(Patient patient) {
        return prescriptionRepository.findByPatient(patient);
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }
}
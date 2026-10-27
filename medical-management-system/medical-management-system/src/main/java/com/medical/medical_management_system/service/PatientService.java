package com.medical.medical_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.repository.PatientRepository;

@Service
public class PatientService {

private final PatientRepository patientRepository;

public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
}

public List<Patient> getAllPatients() {
    return patientRepository.findAll();
}

public Optional<Patient> getPatientById(Long id) {
    return patientRepository.findById(id);
}

public Patient savePatient(Patient patient) {
    return patientRepository.save(patient);
}

public void deletePatient(Long id) {
    patientRepository.deleteById(id);
}

}
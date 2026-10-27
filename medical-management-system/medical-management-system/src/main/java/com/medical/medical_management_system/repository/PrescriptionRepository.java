package com.medical.medical_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.medical_management_system.entity.Doctor;
import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.entity.Prescription;

@Repository
public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatient(Patient patient);

    List<Prescription> findByDoctor(Doctor doctor);
}
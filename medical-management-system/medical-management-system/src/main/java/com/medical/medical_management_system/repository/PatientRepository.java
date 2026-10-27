package com.medical.medical_management_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.entity.User;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUser(User user);

    Optional<Patient> findByEmail(String email);
}
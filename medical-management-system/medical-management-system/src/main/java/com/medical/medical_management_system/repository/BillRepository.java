package com.medical.medical_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.medical_management_system.entity.Bill;
import com.medical.medical_management_system.entity.Patient;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

List<Bill> findByPatient(Patient patient);

}
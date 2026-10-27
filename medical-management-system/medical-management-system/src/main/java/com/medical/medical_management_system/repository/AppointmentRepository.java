package com.medical.medical_management_system.repository;
import com.medical.medical_management_system.entity.Doctor;
import com.medical.medical_management_system.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository
extends JpaRepository<Appointment, Long> {

// Count appointments scheduled for a particular date
long countByAppointmentDate(LocalDate appointmentDate);

// Find appointments belonging to a particular patient
List<Appointment> findByPatientId(Long patientId);
List<Appointment> findByDoctor(Doctor doctor);
}
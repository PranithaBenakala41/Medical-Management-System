package com.medical.medical_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.medical.medical_management_system.entity.Appointment;
import com.medical.medical_management_system.repository.AppointmentRepository;

@Service
public class AppointmentService {

private final AppointmentRepository appointmentRepository;

public AppointmentService(
        AppointmentRepository appointmentRepository) {

    this.appointmentRepository = appointmentRepository;
}

// Get all appointments
public List<Appointment> getAllAppointments() {

    return appointmentRepository.findAll();
}

// Get appointments belonging to a particular patient
public List<Appointment> getAppointmentsByPatientId(Long patientId) {

    return appointmentRepository.findByPatientId(patientId);
}

// Get appointment by ID
public Optional<Appointment> getAppointmentById(Long id) {

    return appointmentRepository.findById(id);
}

// Save an appointment
public Appointment saveAppointment(Appointment appointment) {

    return appointmentRepository.save(appointment);
}

// Delete an appointment
public void deleteAppointment(Long id) {

    appointmentRepository.deleteById(id);
}

}
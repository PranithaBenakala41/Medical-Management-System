package com.medical.medical_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.medical.medical_management_system.entity.Doctor;
import com.medical.medical_management_system.repository.DoctorRepository;
import com.medical.medical_management_system.repository.AppointmentRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Doctor not found."));

        boolean hasAppointments =
                !appointmentRepository.findByDoctor(doctor).isEmpty();

        if (hasAppointments) {
            throw new IllegalArgumentException(
                    "Cannot delete this doctor because appointments are linked to them. "
                    + "Appointment history has been preserved.");
        }

        doctorRepository.delete(doctor);
    }
}
package com.medical.medical_management_system.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "appointments")
public class Appointment {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotBlank(message = "Patient name is required")
private String patientName;

// Link appointment to the patient who booked it
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "patient_id")
private Patient patient;

// Link appointment to the selected doctor
@ManyToOne
@JoinColumn(name = "doctor_id")
private Doctor doctor;

@NotNull(message = "Appointment date is required")
private LocalDate appointmentDate;

private LocalTime appointmentTime;

private String reason;

private String status = "BOOKED";

public Appointment() {
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getPatientName() {
    return patientName;
}

public void setPatientName(String patientName) {
    this.patientName = patientName;
}

public Patient getPatient() {
    return patient;
}

public void setPatient(Patient patient) {
    this.patient = patient;
}

public Doctor getDoctor() {
    return doctor;
}

public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
}

public LocalDate getAppointmentDate() {
    return appointmentDate;
}

public void setAppointmentDate(LocalDate appointmentDate) {
    this.appointmentDate = appointmentDate;
}

public LocalTime getAppointmentTime() {
    return appointmentTime;
}

public void setAppointmentTime(LocalTime appointmentTime) {
    this.appointmentTime = appointmentTime;
}

public String getReason() {
    return reason;
}

public void setReason(String reason) {
    this.reason = reason;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

}
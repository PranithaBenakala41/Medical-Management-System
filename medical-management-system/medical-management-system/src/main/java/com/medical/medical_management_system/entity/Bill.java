package com.medical.medical_management_system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bills")
public class Bill {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "patient_id", nullable = false)
private Patient patient;

private String description;

private double amount;

private LocalDate billDate;

private String status;

public Bill() {
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public Patient getPatient() {
    return patient;
}

public void setPatient(Patient patient) {
    this.patient = patient;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public double getAmount() {
    return amount;
}

public void setAmount(double amount) {
    this.amount = amount;
}

public LocalDate getBillDate() {
    return billDate;
}

public void setBillDate(LocalDate billDate) {
    this.billDate = billDate;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

}
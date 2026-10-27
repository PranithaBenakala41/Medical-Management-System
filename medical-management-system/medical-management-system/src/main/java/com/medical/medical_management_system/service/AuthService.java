package com.medical.medical_management_system.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medical.medical_management_system.entity.Doctor;
import com.medical.medical_management_system.entity.Patient;
import com.medical.medical_management_system.entity.User;
import com.medical.medical_management_system.repository.DoctorRepository;
import com.medical.medical_management_system.repository.PatientRepository;
import com.medical.medical_management_system.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Patient registration
    @Transactional
    public User register(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getPassword() == null
                || user.getPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters");
        }

        String email = user.getEmail().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account already exists with this email");
        }

        user.setEmail(email);
        user.setRole("PATIENT");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        Patient patient = new Patient();
        patient.setName(savedUser.getName());
        patient.setEmail(savedUser.getEmail());
        patient.setUser(savedUser);

        patientRepository.save(patient);

        return savedUser;
    }

    // Create a login account for an existing doctor profile
    @Transactional
    public User createDoctorAccount(String email, String password) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters");
        }

        String normalizedEmail = email.trim();

        Doctor doctor = doctorRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Doctor profile not found"));

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(
                    "An account already exists with this email");
        }

        User user = new User();
        user.setName(doctor.getName());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("DOCTOR");

        return userRepository.save(user);
    }

    // Create doctor profile and doctor login account together
    @Transactional
    public Doctor createDoctorWithAccount(
            Doctor doctor, String password) {

        if (doctor.getName() == null || doctor.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "Doctor name is required");
        }

        if (doctor.getEmail() == null || doctor.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Doctor email is required");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters");
        }

        String email = doctor.getEmail().trim();

        if (doctorRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "A doctor already exists with this email");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account already exists with this email");
        }

        doctor.setEmail(email);

        Doctor savedDoctor = doctorRepository.save(doctor);

        User user = new User();
        user.setName(savedDoctor.getName());
        user.setEmail(savedDoctor.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("DOCTOR");

        userRepository.save(user);

        return savedDoctor;
    }

    // Login validation
    public User login(String email, String password) {

        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            return null;
        }

        User user = userRepository.findByEmail(email.trim())
                .orElse(null);

        if (user != null
                && passwordEncoder.matches(
                        password, user.getPassword())) {
            return user;
        }

        return null;
    }
}
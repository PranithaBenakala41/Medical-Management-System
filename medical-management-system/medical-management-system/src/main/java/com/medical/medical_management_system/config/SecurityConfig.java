package com.medical.medical_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {

    http.authorizeHttpRequests(auth -> auth

        // Public pages and static resources
        .requestMatchers(
            "/", "/login", "/register",
            "/css/**", "/js/**", "/images/**"
        ).permitAll()

        // Only admins can add, edit or delete doctors
        .requestMatchers(
            HttpMethod.GET,
            "/doctors/new", "/doctors/edit/**"
        ).hasRole("ADMIN")

        .requestMatchers(
            HttpMethod.POST,
            "/doctors/save", "/doctors/delete/**"
        ).hasRole("ADMIN")

        // Doctor CRUD list is available to authenticated users
        .requestMatchers(
            HttpMethod.GET, "/doctors"
        ).authenticated()

        // Admin pages
        .requestMatchers("/admin/**").hasRole("ADMIN")

        // Doctor-specific pages
        .requestMatchers("/doctor/**").hasRole("DOCTOR")

        // Patient-specific pages
        .requestMatchers(
            "/patients/dashboard",
            "/patients/profile",
            "/patients/prescriptions",
            "/patients/my-bills",
            "/patients/bills",
            "/patients/appointments"
        ).hasRole("PATIENT")

        .requestMatchers("/patient/**").hasRole("PATIENT")

        // Admin-only patient management
        .requestMatchers(
            "/patients",
            "/patients/new",
            "/patients/save",
            "/patients/edit/**",
            "/patients/delete/**"
        ).hasRole("ADMIN")

        // Dashboard
        .requestMatchers("/dashboard").authenticated()

        // All remaining pages require login
        .anyRequest().authenticated()
    )

    .formLogin(form -> form
        .loginPage("/login")
        .successHandler(roleBasedSuccessHandler())
        .permitAll()
    )

    .logout(logout -> logout
        .logoutSuccessUrl("/login?logout")
        .permitAll()
    );

    return http.build();
}

@Bean
public AuthenticationSuccessHandler roleBasedSuccessHandler() {

    return (request, response, authentication) -> {

        boolean isAdmin = authentication.getAuthorities()
            .stream()
            .anyMatch(authority ->
                authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isDoctor = authentication.getAuthorities()
            .stream()
            .anyMatch(authority ->
                authority.getAuthority().equals("ROLE_DOCTOR"));

        if (isAdmin) {
            response.sendRedirect("/admin/dashboard");
        } else if (isDoctor) {
            response.sendRedirect("/doctor/dashboard");
        } else {
            response.sendRedirect("/patients/dashboard");
        }
    };
}

}
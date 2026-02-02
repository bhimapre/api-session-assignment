package com.example.apiassignment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID employee_id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max =  100, message = "Name must be between 2 and 100 characters long")
    private String name;

    @Email(message = "Email must be in formate")
    @NotBlank(message = "Email is required")
    @Size(min = 7, max =  50, message = "Email must be between 7 and 50 characters long")
    private String email;

    @NotBlank(message = "Phone Number is required")
    @Size(min = 10, max =  10, message = "Phone number size must be 10 digit")
    private String phone_number;

    @NotBlank(message = "Department is required")
    @Size(min = 2, max =  50, message = "Department must be between 2 and 50 characters long")
    private String department;

    @NotBlank(message = "Role is required")
    @Size(min = 2, max =  50, message = "Role must be between 2 and 50 characters long")
    private String role;

    @NotBlank(message = "Role is required")
    @Size(min = 2, max =  50, message = "City must be between 2 and 50 characters long")
    private String city;

    @Size(min = 10, max =  200, message = "Address must be between 10 and 200 characters long")
    private String address;

    @NotNull(message = "Photo is required")
    private String photo;
}

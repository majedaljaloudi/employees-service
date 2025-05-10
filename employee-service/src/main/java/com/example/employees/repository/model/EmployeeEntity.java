package com.example.employees.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private BigDecimal salary;
    private LocalDate joinDate;
    private String department;
}
package com.example.employees.service;

import com.example.employees.controller.model.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    Optional<EmployeeDTO> getEmployeeById(int id);

    List<EmployeeDTO> getEmployees(String name, String fromSalary, String toSalary);

}
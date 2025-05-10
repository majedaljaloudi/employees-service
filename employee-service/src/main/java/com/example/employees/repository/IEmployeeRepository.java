package com.example.employees.repository;

import com.example.employees.repository.model.EmployeeEntity;

import java.util.List;
import java.util.Optional;

public interface IEmployeeRepository {

    public EmployeeEntity save(EmployeeEntity employeeEntity);

    public List<EmployeeEntity> findAll();

    public Optional<EmployeeEntity> findById(int id);
}
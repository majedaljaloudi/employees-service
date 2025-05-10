package com.example.employees.service;

import com.example.employees.controller.model.EmployeeDTO;
import com.example.employees.mapper.EmployeeMapper;
import com.example.employees.repository.EmployeeFileRepository;
import com.example.employees.repository.model.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements IEmployeeService {
    private final EmployeeFileRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeService(EmployeeFileRepository repository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = mapper.toEntity(employeeDTO);
        EmployeeEntity savedEntity = repository.save(employeeEntity);
        return mapper.toDTO(savedEntity);
    }

    public Optional<EmployeeDTO> getEmployeeById(int id) {
        Optional<EmployeeEntity> optionalEmployee = repository.findById(id);
        return optionalEmployee.map(mapper::toDTO);
    }

    public List<EmployeeDTO> getEmployees(String name, String fromSalary, String toSalary) {
        return repository.findAll().stream()
                .filter(emp -> filterByName(emp, name))
                .filter(emp -> filterBySalary(emp, fromSalary, toSalary))
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    private boolean filterByName(EmployeeEntity emp, String name) {
        return name == null || name.isEmpty() ||
                (emp.getFirstName().toLowerCase() + " " + emp.getLastName().toLowerCase())
                        .contains(name.toLowerCase());
    }

    private boolean filterBySalary(EmployeeEntity emp, String fromSalary, String toSalary) {
        BigDecimal employeeSalary = emp.getSalary();
        if (checkFromSalary(fromSalary, employeeSalary)) {
            return false;
        }
        return checkToSalary(toSalary, employeeSalary);
    }

    private boolean checkToSalary(String toSalary, BigDecimal employeeSalary) {
        if (toSalary != null && !toSalary.isEmpty()) {
            try {
                BigDecimal toSalaryValue = new BigDecimal(toSalary);
                if (employeeSalary.compareTo(toSalaryValue) > 0) {
                    return false;
                }
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return true;
    }

    private boolean checkFromSalary(String fromSalary, BigDecimal employeeSalary) {
        if (fromSalary != null && !fromSalary.isEmpty()) {
            try {
                BigDecimal fromSalaryValue = new BigDecimal(fromSalary);
                if (employeeSalary.compareTo(fromSalaryValue) < 0) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
                return true;
            }
        }
        return false;
    }


}

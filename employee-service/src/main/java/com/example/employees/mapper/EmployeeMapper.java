package com.example.employees.mapper;

import com.example.employees.controller.model.EmployeeDTO;
import com.example.employees.repository.model.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeEntity toEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }
        return new EmployeeEntity(
                employeeDTO.getId(),
                employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                employeeDTO.getDateOfBirth(),
                employeeDTO.getSalary(),
                employeeDTO.getJoinDate(),
                employeeDTO.getDepartment()
        );
    }

    public EmployeeDTO toDTO(EmployeeEntity employeeEntity) {
        if (employeeEntity == null)
            return null;

        return new EmployeeDTO(
                employeeEntity.getId(),
                employeeEntity.getFirstName(),
                employeeEntity.getLastName(),
                employeeEntity.getDateOfBirth(),
                employeeEntity.getSalary(),
                employeeEntity.getJoinDate(),
                employeeEntity.getDepartment()
        );
    }
}
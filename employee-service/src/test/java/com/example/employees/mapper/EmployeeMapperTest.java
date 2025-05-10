package com.example.employees.mapper;

import com.example.employees.controller.model.EmployeeDTO;
import com.example.employees.repository.model.EmployeeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EmployeeMapper();
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        EmployeeDTO dto = new EmployeeDTO(
                1,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                new BigDecimal("5000"),
                LocalDate.of(2020, 5, 15),
                "Engineering"
        );

        EmployeeEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getDateOfBirth(), entity.getDateOfBirth());
        assertEquals(dto.getSalary(), entity.getSalary());
        assertEquals(dto.getJoinDate(), entity.getJoinDate());
        assertEquals(dto.getDepartment(), entity.getDepartment());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenInputIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDTO_ShouldMapCorrectly() {
        EmployeeEntity entity = new EmployeeEntity(
                2,
                "Jane",
                "Smith",
                LocalDate.of(1985, 7, 20),
                new BigDecimal("6000"),
                LocalDate.of(2018, 3, 10),
                "HR"
        );

        EmployeeDTO dto = mapper.toDTO(entity);
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getDateOfBirth(), dto.getDateOfBirth());
        assertEquals(entity.getSalary(), dto.getSalary());
        assertEquals(entity.getJoinDate(), dto.getJoinDate());
        assertEquals(entity.getDepartment(), dto.getDepartment());
    }

    @Test
    void toDTO_ShouldReturnNull_WhenInputIsNull() {
        assertNull(mapper.toDTO(null));
    }
}

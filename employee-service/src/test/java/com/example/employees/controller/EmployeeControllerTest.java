package com.example.employees.controller;

import com.example.employees.controller.model.EmployeeDTO;
import com.example.employees.controller.model.IdResponse;
import com.example.employees.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EmployeeControllerTest {

    @Mock
    private EmployeeService service;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private EmployeeDTO buildEmployee(int id) {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(id);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee.setJoinDate(LocalDate.of(2020, 1, 1));
        employee.setSalary(BigDecimal.valueOf(5000));
        employee.setDepartment("Engineering");
        return employee;
    }

    @Test
    void testCreateEmployee() {
        EmployeeDTO input = buildEmployee(0);
        EmployeeDTO saved = buildEmployee(1);

        when(service.createEmployee(input)).thenReturn(saved);

        ResponseEntity<?> response = controller.createEmployee(input);
        assertEquals(200, response.getStatusCode().value());

        IdResponse idResponse = (IdResponse) response.getBody();
        assertNotNull(idResponse);
        assertEquals(1, idResponse.getId());
    }

    @Test
    void testGetEmployeeById_Found() {
        EmployeeDTO employee = buildEmployee(1);

        when(service.getEmployeeById(1)).thenReturn(Optional.of(employee));

        ResponseEntity<?> response = controller.getEmployeeById(1);
        assertEquals(200, response.getStatusCode().value());

        EmployeeDTO result = (EmployeeDTO) response.getBody();
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(service.getEmployeeById(999)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getEmployeeById(999);
        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetEmployees_WithResults() {
        List<EmployeeDTO> mockList = List.of(
                buildEmployee(1),
                buildEmployee(2)
        );

        when(service.getEmployees("John", null, null)).thenReturn(mockList);

        ResponseEntity<List<EmployeeDTO>> response = controller.getEmployees("John", null, null);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetEmployees_EmptyResult() {
        when(service.getEmployees("Nobody", null, null)).thenReturn(Collections.emptyList());

        ResponseEntity<List<EmployeeDTO>> response = controller.getEmployees("Nobody", null, null);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetEmployees_AllParamsNull() {
        ResponseEntity<List<EmployeeDTO>> response = controller.getEmployees(null, null, null);
        assertEquals(204, response.getStatusCode().value());
    }
}

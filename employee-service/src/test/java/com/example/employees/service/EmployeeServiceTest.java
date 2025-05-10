package com.example.employees.service;

import com.example.employees.controller.model.EmployeeDTO;
import com.example.employees.mapper.EmployeeMapper;
import com.example.employees.repository.EmployeeFileRepository;
import com.example.employees.repository.model.EmployeeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

    @Mock
    private EmployeeFileRepository repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEmployee_ShouldReturnSavedDTO() {
        EmployeeDTO dto = new EmployeeDTO();
        EmployeeEntity entity = new EmployeeEntity();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        EmployeeDTO result = service.createEmployee(dto);

        assertEquals(dto, result);
        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper).toDTO(entity);
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenFound() {
        int id = 1;
        EmployeeEntity entity = new EmployeeEntity();
        EmployeeDTO dto = new EmployeeDTO();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        Optional<EmployeeDTO> result = service.getEmployeeById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void getEmployeeById_ShouldReturnEmpty_WhenNotFound() {
        int id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<EmployeeDTO> result = service.getEmployeeById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void getEmployees_ShouldFilterAndReturnList() {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setSalary(new BigDecimal("1000"));

        EmployeeDTO dto = new EmployeeDTO();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        List<EmployeeDTO> result = service.getEmployees("John", "900", "1100");

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getEmployees_ShouldReturnEmpty_WhenNoMatch() {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setFirstName("Jane");
        entity.setLastName("Doe");
        entity.setSalary(new BigDecimal("500"));

        when(repository.findAll()).thenReturn(List.of(entity));

        List<EmployeeDTO> result = service.getEmployees("John", "900", "1100");

        assertTrue(result.isEmpty());
    }
}

package com.example.employees.controller;

import com.example.employees.controller.model.EmployeeDTO;
import com.example.employees.controller.model.IdResponse;
import com.example.employees.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO saved = service.createEmployee(employeeDTO);
        return ResponseEntity.ok().body(new IdResponse(saved.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id) {
        return service.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getEmployees(@RequestParam(required = false) String name, @RequestParam(required = false) String fromSalary, @RequestParam(required = false) String toSalary) {
        if (name == null && fromSalary == null && toSalary == null)
            return ResponseEntity.noContent().build();

        List<EmployeeDTO> employees = service.getEmployees(name, fromSalary, toSalary);
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }
}

package com.src.test.controller;

import com.src.test.Entity.Employee;
import com.src.test.Entity.TaxDetails;
import com.src.test.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.saveEmployee(employee));
    }

    @GetMapping("/{employeeId}/tax")
    public ResponseEntity<TaxDetails> getTaxDetails(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.calculateTax(employeeId));
    }
}

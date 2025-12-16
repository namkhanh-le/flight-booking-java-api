package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Employee;
import com.epita.airlineapi.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET all employees
    @GetMapping
    public ResponseEntity<?> getEmployees() {
        List<Employee> employees = employeeService.getEmployees();

        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // GET one employee by ID
    @GetMapping(path = "{employeeNumber}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long employeeNumber) {
        Optional<Employee> employee = employeeService.getEmployeeById(employeeNumber);

        return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST - Create a new employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    // DELETE - Delete an employee
    @DeleteMapping(path = "{employeeNumber}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeNumber) {
        if (employeeService.getEmployeeById(employeeNumber).isPresent()) {
            employeeService.deleteEmployee(employeeNumber);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // PUT - Update an employee
    @PutMapping(path = "{employeeNumber}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long employeeNumber,
            @RequestBody Employee employeeUpdate
    ) {
        try {
            employeeService.updateEmployee(employeeNumber, employeeUpdate);
            return new ResponseEntity<>("Employee updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
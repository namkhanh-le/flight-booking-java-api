package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Employee;
import com.epita.airlineapi.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Get all employees
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    // Get one employee by ID
    public Optional<Employee> getEmployeeById(Long employeeNumber) {
        return employeeRepository.findById(employeeNumber);
    }

    // Create a new employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Delete an employee
    public void deleteEmployee(Long employeeNumber) {
        boolean exists = employeeRepository.existsById(employeeNumber);

        if (!exists) {
            throw new EntityNotFoundException("Employee with number " + employeeNumber + " does not exist");
        }

        employeeRepository.deleteById(employeeNumber);
    }

    // Update an employee
    @Transactional
    public void updateEmployee(Long employeeNumber, Employee updateRequest) {
        // 1. Retrieve existing Employee
        Employee employee = employeeRepository.findById(employeeNumber)
                .orElseThrow(() -> new IllegalStateException(
                        "Employee with number " + employeeNumber + " does not exist"
                ));

        // 2. Update Profession
        if (updateRequest.getProfession() != null &&
                !updateRequest.getProfession().isEmpty() &&
                !Objects.equals(employee.getProfession(), updateRequest.getProfession())) {
            employee.setProfession(updateRequest.getProfession());
        }

        // 3. Update Title
        if (updateRequest.getTitle() != null &&
                !updateRequest.getTitle().isEmpty() &&
                !Objects.equals(employee.getTitle(), updateRequest.getTitle())) {
            employee.setTitle(updateRequest.getTitle());
        }

        // 4. Update User (relationship)
        if (updateRequest.getUser() != null &&
                !Objects.equals(employee.getUser(), updateRequest.getUser())) {
            employee.setUser(updateRequest.getUser());
        }
    }
}

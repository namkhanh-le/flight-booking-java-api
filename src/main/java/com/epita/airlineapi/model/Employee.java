package com.epita.airlineapi.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeNumber;

    private String profession;

    private String title;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Default constructor (required by JPA)
    public Employee() {
    }

    // Constructor with all fields
    public Employee(Long employeeNumber, String profession, String title, User user) {
        this.employeeNumber = employeeNumber;
        this.profession = profession;
        this.title = title;
        this.user = user;
    }

    // Getters and Setters
    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeNumber, employee.employeeNumber) &&
                Objects.equals(profession, employee.profession) &&
                Objects.equals(title, employee.title) &&
                Objects.equals(user, employee.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber, profession, title, user);
    }
}
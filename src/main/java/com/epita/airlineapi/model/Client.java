package com.epita.airlineapi.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @Column(nullable = false, unique = true)
    private String passportNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔹 Required by JPA
    public Client() {
    }

    public Client(String passportNumber, User user) {
        this.passportNumber = passportNumber;
        this.user = user;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(passportNumber, client.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber);
    }
}

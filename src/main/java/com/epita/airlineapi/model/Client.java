package com.epita.airlineapi.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Client {

    @Id
    private String passportNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

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
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(passportNumber, client.passportNumber) &&
                Objects.equals(user, client.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, user);
    }
}
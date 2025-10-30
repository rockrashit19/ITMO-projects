package com.example.lab34.entity;

import javax.persistence.*;

@Entity @Table(name="USERS")
public class User {
    @Id @GeneratedValue private Long id;
    @Column(unique=true, nullable=false) private String login;
    @Column(nullable=false) private String passwordHash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
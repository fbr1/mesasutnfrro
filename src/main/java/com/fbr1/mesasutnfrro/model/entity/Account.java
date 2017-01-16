package com.fbr1.mesasutnfrro.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="accounts")
public class Account {

    public enum ROLES {
        ADMIN, USER
    }

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_account")
    private Long id;

    @Column(name="name")
    private String name;

    @JsonIgnore
    @Column(name="password")
    private  String password;

    @Column(name="roles")
    private String roles;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    protected Account() {}

    public Account(String name, String password, String... roles) {

        this.name = name;
        this.setPassword(password);
        this.setRoles(roles);
    }

    public String getPassword() {
        return password;
    }

    public static PasswordEncoder getPasswordEncoder() {
        return PASSWORD_ENCODER;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getRoles() {
        List<String> roles = new ArrayList<>();
        for (String s : this.roles.split(",")){
            roles.add(s);
        }
        return roles.stream().toArray(String[]::new);
    }

    public void setRoles(String[] roles) {
        this.roles = String.join(",", roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                '}';
    }
}
package com.greenfoxacademy.TribesBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String fullName;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Kingdom kingdom;
    private String verificationCode;
    private boolean isEmailVerified;
    //Token, email and so on to be added later on
}

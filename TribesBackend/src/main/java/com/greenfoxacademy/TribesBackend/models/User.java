package com.greenfoxacademy.TribesBackend.models;

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
    @OneToOne(cascade=CascadeType.ALL)
    private Kingdom kingdom;
    private String verificationCode;
    private Boolean isEmailVerified;
    //Token, email and so on to be added later on
}

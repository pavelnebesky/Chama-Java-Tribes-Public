package com.greenfoxacademy.TribesBackend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
    @OneToOne
    private Kingdom kingdom;
    //Token, email and so on to be added later on
}

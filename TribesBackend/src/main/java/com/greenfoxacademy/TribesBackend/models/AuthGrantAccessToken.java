package com.greenfoxacademy.TribesBackend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class AuthGrantAccessToken {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    private String accessGrantToken;
    private String facebookId;
}

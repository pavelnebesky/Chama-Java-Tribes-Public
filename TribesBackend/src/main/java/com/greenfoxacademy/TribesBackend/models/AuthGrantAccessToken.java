package com.greenfoxacademy.TribesBackend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "auth_grant_access_token")
public class AuthGrantAccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agat_id")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    private String accessGrantToken;
    private String idExternal;
}

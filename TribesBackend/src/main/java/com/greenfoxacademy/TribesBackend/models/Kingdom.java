package com.greenfoxacademy.TribesBackend.models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Kingdom {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne
    private User user;
}

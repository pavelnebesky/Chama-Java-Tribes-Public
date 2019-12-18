package com.greenfoxacademy.TribesBackend.models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
    @OneToMany
    private List<Building> buildings;
}

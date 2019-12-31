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
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Resource> resources;
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Building> buildings;
}
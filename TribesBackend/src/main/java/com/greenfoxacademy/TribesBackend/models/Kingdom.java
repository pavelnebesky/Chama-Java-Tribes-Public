package com.greenfoxacademy.TribesBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Kingdom {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long userId;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Resource> resources;
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Building> buildings;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Troop> troops;
}
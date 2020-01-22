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
@Table(name = "kingdom")
public class Kingdom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kingdom_id")
    private Long id;
    private String name;
    private Long userId;
    @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
    private List<Resource> resources;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
    private List<Building> buildings;
    @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
    private List<Troop> troops;
}
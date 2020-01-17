package com.greenfoxacademy.TribesBackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;
    private BuildingType type;
    private int level;
    private int hp;
    private Long started_at;
    private Long finished_at;
    @JsonIgnore
    private Long updated_at;
    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "kingdom_id")
    private Kingdom kingdom;
}

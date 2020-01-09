package com.greenfoxacademy.TribesBackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Building {
    @Id
    @GeneratedValue
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
    private Kingdom kingdom;
}

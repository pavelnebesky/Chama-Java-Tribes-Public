package com.greenfoxacademy.TribesBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.TribesBackend.enums.resourceType;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
public class Resource {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private resourceType type;
    private int amount;
    private int generation;
    @ManyToOne
    @JsonIgnore
    private Kingdom kingdom;
}

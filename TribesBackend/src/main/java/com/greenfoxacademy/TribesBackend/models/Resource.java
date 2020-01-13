package com.greenfoxacademy.TribesBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.TribesBackend.enums.ResourceType;
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
public class Resource {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private ResourceType type;
    private int amount;
    private int generation;
    @ManyToOne
    @JsonIgnore
    private Kingdom kingdom;

    public Resource(ResourceType type, int amount, int generation){
        this.type = type;
        this.amount = amount;
        this.generation = generation;
    }
}

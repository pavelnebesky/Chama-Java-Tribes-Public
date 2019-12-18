package com.greenfoxacademy.TribesBackend.models;

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
    private Long id;
    private resourceType type;
    private int amount;
    @ManyToOne
    private Kingdom kingdom;
}

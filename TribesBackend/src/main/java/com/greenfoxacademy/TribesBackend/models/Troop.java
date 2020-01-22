package com.greenfoxacademy.TribesBackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "troop")
public class Troop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "troop_id")
    private long id;
    private int level = 1;
    private int hp;
    private int attack;
    private int defence;
    private long started_at;
    private long finished_at;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kingdom_id")
    private Kingdom kingdom;
}

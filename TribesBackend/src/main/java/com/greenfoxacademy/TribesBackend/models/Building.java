package com.greenfoxacademy.TribesBackend.models;
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
    private enum Type {
        townhall,
        farm,
        mine,
        barracks
    }
    private int level = 1;
    private int hp;
    private Timestamp started_at;
    private Timestamp finished_at;
    @ManyToOne
    private Kingdom kingdom;
}

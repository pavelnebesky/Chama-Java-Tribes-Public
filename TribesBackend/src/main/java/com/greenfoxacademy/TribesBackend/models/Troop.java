package com.greenfoxacademy.TribesBackend.models;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class Troop {
    @Id
    @GeneratedValue
    private long id;
    private int level = 1;
    private int hp;
    private int attack;
    private int defence;
    private long started_at;
    private long finished_at;
    @JsonIgnore
    @ManyToOne
    private Kingdom kingdom;
}
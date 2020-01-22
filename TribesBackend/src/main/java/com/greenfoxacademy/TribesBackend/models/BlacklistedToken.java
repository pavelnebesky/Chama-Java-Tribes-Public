package com.greenfoxacademy.TribesBackend.models;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "black_listed_token")
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blt_id")
    private Long id;
    private String token;
}

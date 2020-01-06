package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Getter
@Setter
@Service
public class TroopService {
    @Autowired
    private TroopRepository troopRepository;
}


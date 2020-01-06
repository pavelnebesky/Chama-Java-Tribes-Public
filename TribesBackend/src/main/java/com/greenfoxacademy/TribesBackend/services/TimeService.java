package com.greenfoxacademy.TribesBackend.services;

import org.springframework.stereotype.Service;

@Service
public class TimeService {

    public double getMinutesBetweenTimeStamps(Long firstTimeStamp, Long secondTimeStamp){
        return Math.abs((firstTimeStamp-secondTimeStamp)/60000.0);
    }
}

package com.greenfoxacademy.TribesBackend.exceptions;

public class InvalidTroopLevelException extends FrontendException {
    public InvalidTroopLevelException(){
        super("Invalid troop level!", 400);
    }
}

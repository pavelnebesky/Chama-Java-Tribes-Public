package com.greenfoxacademy.TribesBackend.exceptions;

public class TownhallLevelTooLowException extends FrontendException{

    public TownhallLevelTooLowException() {
        super("Townhall level too low!", 404);
    }
}

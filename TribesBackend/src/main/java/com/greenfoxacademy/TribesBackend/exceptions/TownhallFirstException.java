package com.greenfoxacademy.TribesBackend.exceptions;

public class TownhallFirstException extends FrontendException{

    public TownhallFirstException() {
        super("Townhall first!", 404);
    }
}
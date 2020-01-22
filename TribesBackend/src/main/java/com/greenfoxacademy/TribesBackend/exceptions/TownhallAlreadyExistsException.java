package com.greenfoxacademy.TribesBackend.exceptions;

public class TownhallAlreadyExistsException extends FrontendException {

    public TownhallAlreadyExistsException() {
        super("Townhall already exists!", 404);
    }
}

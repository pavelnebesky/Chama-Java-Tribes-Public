package com.greenfoxacademy.TribesBackend.exceptions;

public class BarracksNotFoundExeption extends FrontendException {

    public BarracksNotFoundExeption() {
        super("Barracks not found!", 404);
    }
}

package com.greenfoxacademy.TribesBackend.exceptions;

public class NotEnoughGoldException extends FrontendException {

    public NotEnoughGoldException() {
        super("Not enough gold!", 404);
    }
}

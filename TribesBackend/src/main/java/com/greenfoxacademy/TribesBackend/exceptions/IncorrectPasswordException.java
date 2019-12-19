package com.greenfoxacademy.TribesBackend.exceptions;

public class IncorrectPasswordException extends FrontendException {

    public IncorrectPasswordException() {
        super("Incorrect password!", 401);
    }
}

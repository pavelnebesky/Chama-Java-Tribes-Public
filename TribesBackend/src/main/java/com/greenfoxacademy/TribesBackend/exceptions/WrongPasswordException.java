package com.greenfoxacademy.TribesBackend.exceptions;

public class WrongPasswordException extends FrontendException {

    public WrongPasswordException() {
        super("Incorrect password!", 401);
    }
}

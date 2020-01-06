package com.greenfoxacademy.TribesBackend.exceptions;

public class IncorrectVerCodeException extends FrontendException {

    public IncorrectVerCodeException() {
        super("Incorrect verification code!", 404);
    }
}

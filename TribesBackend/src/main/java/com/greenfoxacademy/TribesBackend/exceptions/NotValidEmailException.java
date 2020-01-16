package com.greenfoxacademy.TribesBackend.exceptions;

public class NotValidEmailException extends FrontendException {

    public NotValidEmailException() {
        super("Email is not in a valid format!", 400);
    }
}

package com.greenfoxacademy.TribesBackend.exceptions;

public class EmailAlreadyTakenException extends FrontendException {

    public EmailAlreadyTakenException(String email) {
        super("Email '" + email + "' already taken, please choose an other one.", 409);
    }
}

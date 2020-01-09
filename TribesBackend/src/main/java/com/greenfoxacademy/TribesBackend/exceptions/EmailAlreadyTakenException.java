package com.greenfoxacademy.TribesBackend.exceptions;

public class EmailAlreadyTakenException extends FrontendException {

    public EmailAlreadyTakenException(String username) {
        super("Username '" + username + "' already taken, please choose an other one.", 409);
    }
}

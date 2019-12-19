package com.greenfoxacademy.TribesBackend.exceptions;

public class NoSuchEmailException extends FrontendException {

    public NoSuchEmailException(String email) {
        super("No such email: \'" + email + "\'!", 401);
    }
}

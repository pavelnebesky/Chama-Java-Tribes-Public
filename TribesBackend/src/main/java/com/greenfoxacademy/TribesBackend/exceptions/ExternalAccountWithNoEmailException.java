package com.greenfoxacademy.TribesBackend.exceptions;

public class ExternalAccountWithNoEmailException extends FrontendException {

    public ExternalAccountWithNoEmailException() {
        super("External account cannot provide an email", 400);
    }
}

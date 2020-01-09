package com.greenfoxacademy.TribesBackend.exceptions;

public class FbAccountWithNoEmailException extends FrontendException {

    public FbAccountWithNoEmailException() {
        super("Facebook account cannot provide an email", 400);
    }
}

package com.greenfoxacademy.TribesBackend.exceptions;

public class EmailNotVerifiedException extends FrontendException {

    public EmailNotVerifiedException(){
        super("Email has not been verified yet!", 401);
    }
}

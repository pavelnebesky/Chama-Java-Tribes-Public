package com.greenfoxacademy.TribesBackend.exceptions;

public class EmailAlreadyVerifiedException extends FrontendException {

    public EmailAlreadyVerifiedException(){
        super("Email is already verified!", 400);
    }
}

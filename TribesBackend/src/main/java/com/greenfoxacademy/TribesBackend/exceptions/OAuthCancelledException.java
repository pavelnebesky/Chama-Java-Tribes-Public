package com.greenfoxacademy.TribesBackend.exceptions;

public class OAuthCancelledException extends FrontendException {
    public OAuthCancelledException(){
        super("External login cancelled!", 400);
    }
}

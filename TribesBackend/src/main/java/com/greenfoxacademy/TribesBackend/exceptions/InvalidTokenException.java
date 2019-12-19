package com.greenfoxacademy.TribesBackend.exceptions;

public class InvalidTokenException extends FrontendException {
    public InvalidTokenException(){
        super("Token is invalid!", 401);
    }
}

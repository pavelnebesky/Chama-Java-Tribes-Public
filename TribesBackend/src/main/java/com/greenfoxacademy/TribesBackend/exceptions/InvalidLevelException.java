package com.greenfoxacademy.TribesBackend.exceptions;

public class InvalidLevelException extends FrontendException {

    public InvalidLevelException(String objectName) {
        super("Invalid "+objectName+" level!", 400);
    }
}

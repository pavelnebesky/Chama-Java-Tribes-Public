package com.greenfoxacademy.TribesBackend.exceptions;

public class InvalidBuildingTypeException extends FrontendException {

    public InvalidBuildingTypeException() {
        super("Invalid building type!", 400);
    }
}
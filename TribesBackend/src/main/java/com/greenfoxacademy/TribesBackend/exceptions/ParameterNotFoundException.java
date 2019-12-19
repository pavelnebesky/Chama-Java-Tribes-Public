package com.greenfoxacademy.TribesBackend.exceptions;

public class ParameterNotFoundException extends FrontendException {
    public ParameterNotFoundException(String parameter) {
        super("Parameter " + parameter + " not found!", 404);
    }
}

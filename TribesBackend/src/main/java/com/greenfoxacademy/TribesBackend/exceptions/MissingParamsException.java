package com.greenfoxacademy.TribesBackend.exceptions;

import java.util.List;

public class MissingParamsException extends FrontendException {

    public MissingParamsException(List<String> missingParams) {
        super("Missing parameter(s): " + String.join(", ", missingParams) + "!", 400);
    }
}

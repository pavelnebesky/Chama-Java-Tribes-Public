package com.greenfoxacademy.TribesBackend.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FrontendException extends Exception {

    private int sc;

    public FrontendException(String errorMessage, int sc) {
        super(errorMessage);
        this.sc = sc;
    }
}

package com.greenfoxacademy.TribesBackend.exceptions;

public class IdNotFoundException extends FrontendException {
    public IdNotFoundException(Long id) {
        super("Id " + id + " not found!", 404);
    }
}

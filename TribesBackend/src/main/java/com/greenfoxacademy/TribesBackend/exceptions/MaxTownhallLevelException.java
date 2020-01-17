package com.greenfoxacademy.TribesBackend.exceptions;

public class MaxTownhallLevelException extends FrontendException {

    public MaxTownhallLevelException(int maxLevel) {
        super("Max townhall level is '" + maxLevel + "'!", 400);
    }
}
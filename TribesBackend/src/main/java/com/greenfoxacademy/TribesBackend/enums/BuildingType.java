package com.greenfoxacademy.TribesBackend.enums;

public enum BuildingType {
    townhall("townhall"),
    farm("farm"),
    mine("mine"),
    barracks("barracks");

    private final String text;

    BuildingType( final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

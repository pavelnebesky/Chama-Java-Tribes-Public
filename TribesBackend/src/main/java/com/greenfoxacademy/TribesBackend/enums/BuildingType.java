package com.greenfoxacademy.TribesBackend.enums;

public enum BuildingType {
    townhall("townhall"),
    farm("farm"),
    mine("mine"),
    barracks("barracks");

    private final String text;

    /**
     * @param text
     */
    BuildingType( final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

}
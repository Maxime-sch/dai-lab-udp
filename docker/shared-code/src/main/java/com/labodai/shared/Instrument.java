package com.labodai.shared;

public enum Instrument {
    PIANO("ti-ta-ti"),
    TRUMPET("pouet"),
    FLUTE("trulu"),
    VIOLIN("gzi-gzi"),
    DRUM("boum-boum");

    private final String sound;

    Instrument(String sound) {
        this.sound = sound;
    }

    public String getSound() {
        return this.sound;
    }

    public static Instrument convert(String value){ //TODO is it useful?
        try {
            return Instrument.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
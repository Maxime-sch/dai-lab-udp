package com.labodai.shared;

public enum Instrument {
    PIANO, TRUMPET, FLUTE, VIOLIN, DRUM;

    private final String sound;

    public static void convert(String value){
        Instrument.valueOf(value);
    }
}
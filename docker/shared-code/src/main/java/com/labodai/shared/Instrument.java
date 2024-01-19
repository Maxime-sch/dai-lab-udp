package com.labodai.shared;

public enum Instrument {
    PIANO, TRUMPET, FLUTE, VIOLIN, DRUM;

    private final String sound;

    public String getSound() {
        
        switch(this) {
      
      case PIANO:
        return "ti-ta-ti";

      case TRUMPET:
        return "pouet";

      case FLUTE:
        return "trulu";

      case VIOLIN:
        return "gzi-gzi";
      
      case DRUM:
        return "boum-boum";

      default:
        return null;
      }
    }

    public static void convert(String value){
        Instrument.valueOf(value);
    }
}

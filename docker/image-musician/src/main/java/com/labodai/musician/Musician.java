package com.labodai.musician;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;

import static java.nio.charset.StandardCharsets.*;

class Musician {
    private final Instrument instrument;
    private final UUID uuid;

    public Musician(Instrument instrument) {
        this.instrument = instrument;
        uuid = UUID.randomUUID();
    }

    static class MusicianInfo { // this inner class contains the information we want to serialize using GSON
        private final UUID uuid;
        private final String sound;

        public MusicianInfo(UUID uuid, String sound) {
            this.uuid = uuid;
            this.sound = sound;
        }
    }

    public String toString() { // JSON serialization of our musician playing a sound
        MusicianInfo musicianInfo = new MusicianInfo(uuid, instrument.getSound());
        Gson gson = new Gson();
        return gson.toJson(musicianInfo);
    }

    class PlayInstrument extends TimerTask { // inner class to send UDP packets with the serialization defined in toString
        private final DatagramSocket socket;
        private final DatagramPacket packet;
        private final String message;

        public PlayInstrument() throws IOException {
            socket = new DatagramSocket();
            message = Musician.this.toString();
            byte[] payload = message.getBytes(UTF_8);
            InetSocketAddress dest_address = new InetSocketAddress("239.255.22.5", 9904);
            packet = new DatagramPacket(payload, payload.length, dest_address);
        }
        public void run() {
            try {
                  socket.send(packet);
                  System.out.println("Emitted sound");
            } catch (IOException ex) {
                 System.out.println(ex.getMessage());
             }        
        }
    }

    public static Instrument getInstrumentFromArgs(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the instrument as a command-line argument.");
            return null;
        }

        try {
            return Instrument.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid instrument. Please provide a valid instrument.");
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        Instrument instrument = getInstrumentFromArgs(args);
        Musician musician = new Musician(instrument);
        var executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(musician.new PlayInstrument(), 0, 1, TimeUnit.SECONDS);
    }
}

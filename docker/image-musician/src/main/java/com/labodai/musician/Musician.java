package com.labodai.musician;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

class Musician {
    @Expose
    @SerializedName("sound")
    private final Instrument instrument;
    @Expose
    private final UUID uuid;
    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Instrument.class, (JsonSerializer<Instrument>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getSound()))
            .create();

    public Musician(Instrument instrument) {
        this.instrument = instrument;
        uuid = UUID.randomUUID();
    }

    public static Instrument getInstrumentFromArgs(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the instrument as a command-line argument.");
            System.exit(1);
        }

        try {
            return Instrument.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid instrument. Please provide a valid instrument.");
            System.exit(1);
        }

        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Instrument instrument = getInstrumentFromArgs(args);
        Musician musician = new Musician(instrument);
        musician.play();
    }

    public String toString() {
        return gson.toJson(this);
    }

    public void play() throws IOException, InterruptedException {
        try (var socket = new DatagramSocket()) {
            var destAddress = new InetSocketAddress("239.255.22.5", 9904);
            var payload = toString().getBytes(UTF_8);
            var packet = new DatagramPacket(
                    payload,
                    payload.length,
                    destAddress
            );

            while (true) {
                socket.send(packet);
                System.out.println("Emitted sound");
                Thread.sleep(Duration.ofSeconds(1));
            }
        }
    }
}

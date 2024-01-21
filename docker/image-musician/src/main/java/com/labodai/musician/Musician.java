package com.labodai.musician;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.*;

class Musician {
    private final Instrument instrument;
    private final UUID uuid;

    public Musician(Instrument instrument) {
        this.instrument = instrument;
        uuid = UUID.randomUUID();
    }

    // TODO burk, see https://studytrails.com/2016/09/12/java-google-json-parse-json-to-java/
   public String toString() { // JSON serialization of our musician playing a sound
      return "{\"uuid\": \"" + uuid.toString() + "\", \"sound\": \" " + instrument.getSound() + "\"}";
   }  

    class PlayInstrument extends TimerTask { //sends UDP packets with the aforementioned serialization
        public void run() {
            // TODO inefficient to open a new socket everytime you send a message, please fix
            // also you can use the same datagrame everytime
            try (DatagramSocket socket = new DatagramSocket()) {
                  String message = Musician.this.toString();
                  byte[] payload = message.getBytes(UTF_8);
                  InetSocketAddress dest_address = new InetSocketAddress("239.255.22.5", 9904);
                  DatagramPacket packet = new DatagramPacket(payload, payload.length, dest_address);
                  socket.send(packet);
                  System.out.println("Emitted sound");
             } catch (IOException ex) {
                 System.out.println(ex.getMessage());
             }        
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the instrument as a command-line argument.");
            return;
        }

        // TODO love that, but extract it to a function
        Instrument instrument;
        try {
            instrument = Instrument.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid instrument. Please provide a valid instrument.");
            return;
        }
        Musician musician = new Musician(instrument);
        // TODO I would replace the time with this. This is a more standard way of doing it
        //        var executor = Executors.newSingleThreadScheduledExecutor();
        //        executor.scheduleAtFixedRate(() -> System.out.println("HEllo"), 0, 1, TimeUnit.SECONDS);
        // or you could just have a while true loop with a Thread.sleep of 5seconds since you don't need to do anything else on the main thread
        Timer timer = new Timer();
        timer.schedule(musician.new PlayInstrument(), 0, 1000); // Schedule the PlayInstrument task
        // TODO: unexpected ahah
        Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
    }
}

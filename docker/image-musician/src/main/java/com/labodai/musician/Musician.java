package com.labodai.musician;

import com.labodai.shared.Instrument;
import com.labodai.shared.UdpConstants;
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
    private final String uuid;

    public Musician(Instrument instrument) {
        this.instrument = instrument;
        uuid = UUID.randomUUID().toString();
    }

   public String toString() { 
      return "{\"uuid\": \"" + uuid + "\", \"sound\": \" " + instrument.getSound() + "\"}"; 
   }  

    class PlayInstrument extends TimerTask {
        public void run() {
            try (DatagramSocket socket = new DatagramSocket()) {
                  String message = Musician.this.toString();
                  byte[] payload = message.getBytes(UTF_8);
                  InetSocketAddress dest_address = new InetSocketAddress(UdpConstants.IP, UdpConstants.PORT);
                  DatagramPacket packet = new DatagramPacket(payload, payload.length, dest_address);
                  socket.send(packet);
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

        Instrument instrument;
        try {
            instrument = Instrument.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid instrument. Please provide a valid instrument.");
            return;
        }
        Musician musician = new Musician(instrument);
        Timer timer = new Timer();
        timer.schedule(musician.new PlayInstrument(), 0, 1000); // Schedule the PlayInstrument task
        Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
    } //TODO how and when to stop the musician??
}

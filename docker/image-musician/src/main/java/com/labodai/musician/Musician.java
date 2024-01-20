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
        Instrument instrument = Instrument.PIANO; // Choose the instrument for the musician we're going to create
        Musician musician = new Musician(instrument);
        Timer timer = new Timer();
        timer.schedule(musician.new PlayInstrument(), 0, 1000); // Schedule the PlayInstrument task
        Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
    }
}

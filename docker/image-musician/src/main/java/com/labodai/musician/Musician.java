package com.labodai.musician;

import com.google.gson.Gson;
import com.labodai.shared.Instrument;
import com.labodai.shared.UdpConstants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import static java.nio.charset.StandardCharsets.*;

class Musician {
    private Instrument instrument;
    private String uuid;

    public Musician(Instrument instrument) {
        this.instrument = instrument;
        uuid = ""; //TODO
    }

   public String toString() { 
      return "{\"uuid\": \"" + uuid + "\", \"sound\": \" " + instrument.getSound() + "\"}"; 
   }  

    class PlayInstrument extends TimerTask {
        public void run() {
            try (DatagramSocket socket = new DatagramSocket()) {
                  String message = this.toString();
                  byte[] payload = message.getBytes(UTF_8);
                  InetSocketAddress dest_address = new InetSocketAddress(UdpConstants.IP, UdpConstants.PORT);
                  var packet = new DatagramPacket(payload, payload.length, dest_address);
                  socket.send(packet);
             } catch (IOException ex) {
                 System.out.println(ex.getMessage());
             }        
        }
    }

    public static void main(String[] args) {
        Instrument test;
        Timer timer = new Timer();
        timer.schedule(new PlayInstrument(), 0, 1000);
    }
}

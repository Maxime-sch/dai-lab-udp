package com.labodai.musician;

import com.google.gson.Gson;
import com.labodai.shared.Instrument;
import com.labodai.shared.UdpConstants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import static java.nio.charset.StandardCharsets.*;

class Musician{
    Instrument sound;
    String uuid;

    public Musician(Instrument sound){
        this.sound = sound;
        uuid = ""; //TODO
    }

    public void playInstrument(){

    }

    public static void main(String[] args) {
        Instrument test;
    }
}

/*
class MulticastSender {
    final static String IPADDRESS = UdpConstants.;
    final static int PORT = 44444;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {

            String message = "Hello group members!";
            byte[] payload = message.getBytes(UTF_8);
            InetSocketAddress dest_address = new InetSocketAddress(IPADDRESS, 44444);
            var packet = new DatagramPacket(payload, payload.length, dest_address);
            socket.send(packet);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}*/

package com.labodai.auditor;

import com.google.gson.Gson;
import com.labodai.shared.UdpConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;

public class MusicianListener implements Runnable {
    private static final Gson gson = new Gson();
    private static final InetSocketAddress address = new InetSocketAddress(UdpConstants.IP, UdpConstants.PORT);
    private final MusicianState state;

    public MusicianListener(MusicianState state) {
        this.state = state;
    }

    private void joinGroupOnAllInterfaces(MulticastSocket socket) throws IOException {
        var iter = NetworkInterface.getNetworkInterfaces();

        while (iter.hasMoreElements()) {
            var netif = iter.nextElement();
            socket.joinGroup(address, netif);
        }
    }

    private MusicianMessage receiveNextMusicianMessage(DatagramPacket datagramPacket, MulticastSocket socket) throws IOException {
        var originalBufferSize = datagramPacket.getLength();
        socket.receive(datagramPacket);
        var rawPayload = new String(
                datagramPacket.getData(),
                0,
                datagramPacket.getLength(),
                StandardCharsets.UTF_8
        );

        var payload = gson.fromJson(rawPayload, MusicianMessage.class);
        datagramPacket.setLength(originalBufferSize);
        return payload;
    }

    @Override
    public void run() {
        try(var socket = new MulticastSocket(address.getPort())) {
            joinGroupOnAllInterfaces(socket);

            var buffer =  new byte[1024];
            var datagram = new DatagramPacket(buffer, 0, buffer.length);

            while (true) {
                var message = receiveNextMusicianMessage(datagram, socket);
                System.out.println(message);
                state.onMusicianHeard(message.uuid(), message.sound());
            }

            // socket.leaveGroup(address, netif);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record MusicianMessage(String uuid, String sound) {};
}

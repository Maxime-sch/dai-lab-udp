package com.labodai.auditor;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

class Auditor {
    private static final Gson gson = new Gson();

    private static void handleAuditorClientRequest(MusicianState state, ServerSocket serverSocket) throws IOException {
        try (var socket = serverSocket.accept();
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            out.write(gson.toJson(state.getMusicians()));
        }
    }

    public static void main(String[] args) {
        var state = new MusicianState();
        var listener = new MusicianListener(state);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor();
             var socket = new ServerSocket(2205)) {
            // Create a UDP client listening for musician messages
            executor.submit(listener);

            // Serve active musician list on TCP socket
            // TODO make implementation a multithreading server based on virtual thread
            while (true) {
                handleAuditorClientRequest(state, socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
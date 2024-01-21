package com.labodai.auditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.Executors;

class Auditor {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toEpochMilli()))
            .registerTypeAdapter(Instrument.class, (JsonSerializer<Instrument>) (src, typeOfSrc, context) -> new JsonPrimitive(src.name().toLowerCase()))
            .create();

    private static void handleAuditorClientRequest(MusicianState state, ServerSocket serverSocket) throws IOException {
        try (var socket = serverSocket.accept();
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            var activeMusician = state.getMusicians();
            System.out.println("Serving list of active musician, count " + activeMusician.size());
            gson.toJson(activeMusician, out);
            out.flush();
        }
    }

    public static void main(String[] args) {
        var state = new MusicianState();
        var listener = new MusicianListener(state);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor();
             var socket = new ServerSocket(2205)) {
            // Create a UDP client listening for musician messages
            executor.submit(listener);
            System.out.println("Started listening for musician message");

            // Serve active musician list on TCP socket
            while (true) {
                handleAuditorClientRequest(state, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
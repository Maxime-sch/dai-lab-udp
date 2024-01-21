package com.labodai.auditor;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Auditor {
    public static void main(String[] args) {
        var state = new MusicianState();
        var listener = new MusicianListener(state);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Create a UDP client listening for musician messages
            executor.submit(listener);


            // Musician expiration thread
            // TODO musician API


            executor.awaitTermination(100000, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
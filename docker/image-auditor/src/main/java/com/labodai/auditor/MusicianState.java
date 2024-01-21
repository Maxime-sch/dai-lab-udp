package com.labodai.auditor;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class MusicianState {
    private static final Duration MUSICIAN_TIMEOUT = Duration.ofSeconds(5);
    private final Set<Musician> musicians = new HashSet<>();

    private synchronized void expireMusicians() {
        var now = Instant.now();
        musicians.stream()
                .filter(musician -> musician.getLastHeard().plus(MUSICIAN_TIMEOUT).isBefore(now))
                // copy list of expired musician to avoid concurrent modification exception
                .toList()
                .forEach(musician -> {
                    System.out.println("Expiring musician " + musician.getUuid());
                    musicians.remove(musician);
                });
    }

    public Set<Musician> getMusicians() {
        expireMusicians();
        var now = Instant.now();
        return musicians.stream()
                // musician must have been heard for some time before appearing in the list
                //TODO understand why test fail with that .filter(musician -> musician.getFirstHeard().isBefore(now.minus(MUSICIAN_TIMEOUT)))
                .collect(Collectors.toUnmodifiableSet());
    }

    public synchronized void onMusicianHeard(String uuid, String sound) {
        var matchingMusician = musicians.stream()
                        .filter(musician -> musician.getUuid().equals(uuid))
                        .findFirst();

        if(matchingMusician.isPresent()) {
            matchingMusician.get().setLastHeard(Instant.now());
        } else {
            musicians.add(new Musician(
                    uuid,
                    Arrays.stream(Instrument.values())
                            .filter(instrument -> instrument.getSound().equals(sound.trim()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("This sound is not recognized " + sound)),
                    Instant.now(),
                    Instant.now()
            ));
        }
    }


}

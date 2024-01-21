package com.labodai.auditor;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
        return Collections.unmodifiableSet(musicians);
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

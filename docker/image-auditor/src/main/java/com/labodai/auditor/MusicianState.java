package com.labodai.auditor;

import com.labodai.shared.Instrument;

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
                .filter(musician -> musician.lastHeard.plus(MUSICIAN_TIMEOUT).isAfter(now))
                // copy list of expired musician to avoid concurrent modification exception
                .toList()
                .forEach(musicians::remove);
    }

    public Set<Musician> getMusicians() {
        expireMusicians();
        var now = Instant.now();
        return musicians.stream()
                // musician must have been heard for some time before appearing in the list
                .filter(musician -> musician.getFirstHeard().isBefore(now.minus(MUSICIAN_TIMEOUT)))
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

    public static final class Musician {
        private final String uuid;
        private final Instrument instrument;
        private Instant firstHeard;
        private Instant lastHeard;

        public Musician(String uuid, Instrument instrument, Instant firstHeard, Instant lastHeard) {
            this.uuid = uuid;
            this.instrument = instrument;
            this.firstHeard = firstHeard;
            this.lastHeard = lastHeard;
        }

        public String getUuid() {
            return uuid;
        }

        public Instrument getInstrument() {
            return instrument;
        }

        public Instant getFirstHeard() {
            return firstHeard;
        }

        public void setFirstHeard(Instant firstHeard) {
            this.firstHeard = firstHeard;
        }

        public Instant getLastHeard() {
            return lastHeard;
        }

        public void setLastHeard(Instant lastHeard) {
            this.lastHeard = lastHeard;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Musician musician = (Musician) o;
            return Objects.equals(uuid, musician.uuid) && instrument == musician.instrument;
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, instrument);
        }
    }
}

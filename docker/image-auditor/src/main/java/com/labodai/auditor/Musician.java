package com.labodai.auditor;

import java.time.Instant;
import java.util.Objects;

public final class Musician {
    private final String uuid;
    private final Instrument instrument;
    private Instant firstActivity;
    private Instant lastActivity;

    public Musician(String uuid, Instrument instrument, Instant firstActivity, Instant lastActivity) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.firstActivity = firstActivity;
        this.lastActivity = lastActivity;
    }

    public String getUuid() {
        return uuid;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Instant getFirstHeard() {
        return firstActivity;
    }

    public void setFirstHeard(Instant firstHeard) {
        this.firstActivity = firstHeard;
    }

    public Instant getLastHeard() {
        return lastActivity;
    }

    public void setLastHeard(Instant lastHeard) {
        this.lastActivity = lastHeard;
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
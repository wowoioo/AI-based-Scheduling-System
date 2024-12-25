package com.github.rayinfinite.scheduler.GAcourse;

import java.util.Date;

public class Timeslot {
    private final int timeslotId;
    private final Date timeslot;

    public Timeslot(int timeslotId, Date timeslot) {
        this.timeslot = timeslot;
        this.timeslotId = timeslotId;
    }

    public int getTimeslotId() {
        return this.timeslotId;
    }

    public Date getTimeslot() {
        return this.timeslot;
    }
}

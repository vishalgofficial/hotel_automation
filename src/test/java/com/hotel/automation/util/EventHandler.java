package com.hotel.automation.util;

import com.hotel.automation.listener.Event;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    private int numFloors;
    private int numMainCorridors;
    private int numSubCorridors;

    private String mode;

    private List<Event> events = new ArrayList<>();

    public int getNumFloors() {
        return numFloors;
    }

    public void setNumFloors(int numFloors) {
        this.numFloors = numFloors;
    }

    public int getNumMainCorridors() {
        return numMainCorridors;
    }

    public void setNumMainCorridors(int numMainCorridors) {
        this.numMainCorridors = numMainCorridors;
    }

    public int getNumSubCorridors() {
        return numSubCorridors;
    }

    public void setNumSubCorridors(int numSubCorridors) {
        this.numSubCorridors = numSubCorridors;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

package com.hotel.automation.controller;

import com.hotel.automation.listener.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class HotelControllerTest {
    List<Event> events;

    @BeforeEach
    void setUp() {
        events = new ArrayList<>();
        Event event = new Event();
        event.setFloorNumber(1);
        event.setCorridorId("SubCorridor1");
        event.setMovementDetected(true);
        Event event2 = new Event();
        event2.setFloorNumber(1);
        event2.setCorridorId("SubCorridor1");
        event2.setMovementDetected(false);
        events.add(event);
        events.add(event2);
    }

    @Test
    void test() {
        HotelController hotelController = new HotelController(2, 1, 2);
        hotelController.actOnMovementEvent(events);
    }


}
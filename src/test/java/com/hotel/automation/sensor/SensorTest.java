package com.hotel.automation.sensor;

import com.hotel.automation.controller.HotelController;
import com.hotel.automation.corridors.SubCorridor;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.listener.Event;
import com.hotel.automation.util.SensorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensorTest {
    List<Event> events;

    @BeforeEach
    void setUp() {
        events = new ArrayList<>();
        Event event = new Event();
        event.setFloorNumber(1);
        event.setCorridorId("SubCorridor1");
        event.setMovementDetected(true);
        events.add(event);
    }

    @Test
    public void testMovementDetectionInSubCorridor() {
        HotelController hotelController = new HotelController(1, 1, 1);
        hotelController.actOnMovementEvent(events);
        assertTrue(hotelController.getHotel().getFloors().get(0).getCorridorById("SubCorridor1").get().isMovementDetected());
    }

    @Test
    public void testLightsOnDuringMovement() {
        HotelController hotelController = new HotelController(1, 1, 2);
        hotelController.onEventDetected(SensorUtil.generateSensorId(new Floor(1), new SubCorridor(1)), true);
        assertTrue(hotelController.getHotel().getFloors().get(0).getCorridorById("SubCorridor1").get().getEquipments().get(1).isOn());
    }

    @Test
    public void testNoMovementDetectionInSubCorridor() {
        events.get(0).setMovementDetected(false);
        HotelController hotelController = new HotelController(1, 1, 1);
        hotelController.actOnMovementEvent(events);
        assertFalse(hotelController.getHotel().getFloors().get(0).getCorridorById("SubCorridor1").get().isMovementDetected());
    }

    @Test
    public void testLightsOffDuringNoMovement() {
        HotelController hotelController = new HotelController(1, 1, 2);
        SubCorridor subCorridor = new SubCorridor(1);
        hotelController.onEventDetected(SensorUtil.generateSensorId(new Floor(1), subCorridor), false);
        assertTrue(hotelController.getHotel().getFloors().get(0).getCorridorById("SubCorridor1").get().getEquipments().get(0).isOff());
    }
}
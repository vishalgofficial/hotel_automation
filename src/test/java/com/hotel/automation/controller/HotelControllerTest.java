package com.hotel.automation.controller;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.corridors.SubCorridor;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.hotel.Hotel;
import com.hotel.automation.listener.Event;
import com.hotel.automation.listener.EventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.hotel.automation.equipment.State.OFF;
import static com.hotel.automation.equipment.State.ON;
import static com.hotel.automation.equipment.Type.AC;
import static com.hotel.automation.equipment.Type.LIGHT;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

class HotelControllerTest {
    private List<Event> events;

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

    @Test
    void check_AreCorridorsRegisteredToSenseEvent() {
        HotelController hotelController = new HotelController(2, 1, 2);
        Hotel hotel = hotelController.getHotel();
        EventListener eventListener = hotel.getFloors().get(0).getCorridors().get(0).getSensor().getEventListener();
        assertTrue(eventListener instanceof HotelController);
    }

    @Test
    void verify_unregisterFromSensorEvents() {
        HotelController hotelController = new HotelController(2, 1, 2);
        Hotel hotel = hotelController.getHotel();
        hotelController.unregisterFromSensorEvents();
        EventListener eventListener = hotel.getFloors().get(0).getCorridors().get(0).getSensor().getEventListener();
        assertFalse(eventListener instanceof HotelController);
    }

    @Test
    void verifyDefaultStateOfEquipments_MainCorridor() {
        HotelController hotelController = new HotelController(2, 1, 2);
        Hotel hotel = hotelController.getHotel();
        List<Corridor> mainCorridors = hotel.getFloors().get(0).getCorridors()
                .stream().filter(g -> g instanceof MainCorridor).collect(toList());
        mainCorridors.get(0).getEquipments().forEach(equipment -> {
            if (equipment.getType().equals(LIGHT)) {
                assertEquals(ON, equipment.getState());
            } else if (equipment.getType().equals(AC)) {
                assertEquals(ON, equipment.getState());
            }
        });
    }

    @Test
    void verifyDefaultStateOfEquipments_SubCorridor() {
        HotelController hotelController = new HotelController(2, 1, 2);
        Hotel hotel = hotelController.getHotel();
        List<Corridor> subCorridors = hotel.getFloors().get(0).getCorridors()
                .stream().filter(g -> g instanceof SubCorridor).collect(toList());
        subCorridors.get(0).getEquipments().forEach(equipment -> {
            if (equipment.getType().equals(LIGHT)) {
                assertEquals(OFF, equipment.getState());
            } else if (equipment.getType().equals(AC)) {
                assertEquals(ON, equipment.getState());
            }
        });
    }

    @Test
    public void testCompensatePowerConsumption() {
        events = new ArrayList<>();
        Event event = new Event();
        event.setFloorNumber(1);
        event.setCorridorId("SubCorridor1");
        event.setMovementDetected(true);
        HotelController hotelController = new HotelController(1, 1, 1);
        hotelController.actOnMovementEvent(events);
        Floor floor = hotelController.getHotel().getFloors().get(0);
        assertTrue(floor.getPowerConsumption() <= floor.getMaxPowerConsumption());
    }

    @Test
    public void totalPowerConsumptionShouldNotBeMoreThatMaxPowerConsumptionWhenMovementDetected() {
        HotelController hotelController = new HotelController(1, 1, 2);
        hotelController.actOnMovementEvent(events);
        Hotel hotel = hotelController.getHotel();
        assertTrue(hotelController.getHotel().getFloors().get(0).getCorridorById("SubCorridor1").get().getEquipments().get(1).isOn());
        assertEquals(25, hotel.getFloors().get(0).getPowerConsumption());
    }

    @Test
    public void totalPowerDifferenceShouldBe5UnitWhenMovementDetected() {
        HotelController hotelController = new HotelController(1, 1, 1);
        hotelController.actOnMovementEvent(events);
        Hotel hotel = hotelController.getHotel();
        assertEquals(5, hotel.getFloors().get(0).getPowerDifference());
    }

    @Test
    public void totalPowerConsumptionShouldBe35WhenNoMovementDetected() {
        events = new ArrayList<>();
        Event event = new Event();
        event.setFloorNumber(1);
        event.setCorridorId("SubCorridor1");
        event.setMovementDetected(false);
        HotelController hotelController = new HotelController(1, 1, 2);
        hotelController.actOnMovementEvent(events);
        Hotel hotel = hotelController.getHotel();
        assertTrue(hotelController.getHotel().getFloors().get(0).getCorridorById("SubCorridor1").get().getEquipments().get(1).isOn());
        assertEquals(35, hotel.getFloors().get(0).getPowerConsumption());
    }

    @Test
    public void totalPowerDifferenceShouldBe0UnitWhenMovementDetected() {
        HotelController hotelController = new HotelController(1, 1, 1);
        events.get(0).setMovementDetected(false);
        hotelController.actOnMovementEvent(events);
        Hotel hotel = hotelController.getHotel();
        assertEquals(0, hotel.getFloors().get(0).getPowerDifference());
    }


}
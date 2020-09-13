package com.hotel.automation.controller;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.equipment.Type;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.hotel.Hotel;
import com.hotel.automation.hotel.HotelRequest;
import com.hotel.automation.listener.Event;
import com.hotel.automation.listener.EventListener;

import java.util.List;
import java.util.Random;
import java.util.Stack;

import static com.hotel.automation.util.AutomationUtil.getCorridorFromSensorId;
import static com.hotel.automation.util.AutomationUtil.getFloorFromSensorId;
import static com.hotel.automation.util.PrintUtil.printHotelInfo;

public class HotelController implements EventListener {
    private Hotel hotel;

    public HotelController(int floorCount, int mainCorridorCount, int subCorridorCount) {
        this.hotel = new Hotel(floorCount, mainCorridorCount, subCorridorCount);
        registerCorridorsToSensEvents();
        initializeDefaultState();
        printHotelInfo(hotel);

    }

    public HotelController(HotelRequest hotelRequest) {
        this.hotel = new Hotel(hotelRequest.getNumFloors(), hotelRequest.getNumMainCorridors(), hotelRequest.getNumSubCorridors());
        registerCorridorsToSensEvents();
        initializeDefaultState();
        printHotelInfo(hotel);
    }

    public void registerCorridorsToSensEvents() {
        for (Floor floor : hotel.getFloors()) {
            List<Corridor> corridors = floor.getCorridors();
            for (Corridor corridor : corridors) {
                if (corridor != null) {
                    if (corridor.getSensor() != null) {
                        corridor.getSensor().registerListener(this);
                    }
                }
            }
        }
    }

    public void initializeDefaultState() {
        hotel.getFloors().forEach(floor -> floor.getCorridors().forEach(Corridor::setDefaultState));
    }

    public void actOnMovementEvent(List<Event> events) {
        for (Event event : events) {
            hotel.getFloorByNumber(event.getFloorNumber())
                    .flatMap(floor -> floor.getCorridorById(event.getCorridorId()))
                    .ifPresent(corridor -> corridor.getSensor().eventDetected(event.isMovementDetected()));
        }
    }

    @Override
    public void onEventDetected(String sensorId, boolean detected) {
        Floor floor = getFloorFromSensorId(sensorId, hotel);
        Corridor corridor = getCorridorFromSensorId(sensorId, floor);
        if (detected) {
            handleMovement(floor, corridor);
        } else {
            handleNoMovement(floor, corridor);
        }
    }

    private void handleMovement(Floor floor, Corridor corridor) {
        System.out.println("Movement detected at Floor number " + floor.getFloorNumber() + ", Corridor number " + corridor.getId());
        corridor.setMovementDetected(true);
        controlEquipmentsOnMovement(floor, corridor);
        printHotelInfo(hotel);
    }

    private void controlEquipmentsOnMovement(Floor floor, Corridor corridor) {
        corridor.getEquipments().forEach(equipment -> {
            corridor.getEquipmentStack().push(equipment);
            equipment.switchOn();
            System.out.println("Switching on " + equipment.getType() + " of Floor " + floor.getFloorNumber() + ", Corridor " + corridor.getId());
        });
        long difference = getPowerDifference(floor);
        if (difference > 0) {
            compensatePowerConsumption(floor, corridor, difference);
        }
    }

    private void compensatePowerConsumption(Floor floor, Corridor corridor, long power) {
        List<Corridor> corridors = floor.getCorridors();
        corridors.removeIf(corr -> corr instanceof MainCorridor);
        if (corridors.size() > 1) {
            corridors.remove(corridor);
        }
        int numCorridors = corridors.size();
        while (numCorridors > 0 && power > 0) {
            Random random = new Random();
            int corridorIndex = random.nextInt(numCorridors);
            Corridor chosenCorridor = corridors.get(corridorIndex);
            List<Equipment> equipments = chosenCorridor.getEquipments();
            for (Equipment equipment : equipments) {
                corridor.getEquipmentStack().push(equipment);
                equipment.switchOff();
                System.out.println("Switching off " + equipment.getType() + " of Floor " + floor.getFloorNumber() + ", Corridor " + chosenCorridor.getId());
                power -= equipment.getPowerUnits();
                if (power <= 0)
                    break;
            }
            numCorridors--;
        }
    }

    private void handleNoMovement(Floor floor, Corridor corridor) {
        System.out.println(" No Movement detected at Floor number " + floor.getFloorNumber() + ", Corridor number " + corridor.getId());
        corridor.setMovementDetected(false);
        controlEquipmentsNoOnMovement(floor, corridor);
        printHotelInfo(hotel);
    }

    private void controlEquipmentsNoOnMovement(Floor floor, Corridor corridor) {
        revertStateChange(corridor.getEquipmentStack());
    }

    private long getPowerDifference(Floor floor) {
        long difference = floor.getPowerConsumption() - floor.getMaxPowerConsumption();
        if (difference > 0) {
            System.out.println("Power consumption is " + floor.getPowerConsumption() + " units");
            System.out.println("Power consumption exceeded by " + difference + " units");
        }
        return difference;
    }

    private void revertStateChange(Stack<Equipment> stateChangeTrail) {
        while (!stateChangeTrail.isEmpty()) {
            Equipment equipment = stateChangeTrail.pop();
            if (equipment.getType().equals(Type.LIGHT)) {
                equipment.switchOff();
            } else {
                equipment.switchOn();
            }
        }
    }
}

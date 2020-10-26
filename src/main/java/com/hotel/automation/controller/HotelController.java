package com.hotel.automation.controller;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.hotel.Hotel;
import com.hotel.automation.hotel.HotelRequest;
import com.hotel.automation.listener.Event;
import com.hotel.automation.listener.EventListener;

import java.util.*;

import static com.hotel.automation.equipment.Priority.HIGH;
import static com.hotel.automation.equipment.Type.LIGHT;
import static com.hotel.automation.util.AutomationUtil.getCorridorFromSensorId;
import static com.hotel.automation.util.AutomationUtil.getFloorFromSensorId;
import static com.hotel.automation.util.PrintUtil.printHotelInfo;
import static java.util.Collections.sort;

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
        hotel.getFloors().stream().map(Floor::getCorridors)
                .forEach(corridors -> corridors.stream()
                        .filter(Objects::nonNull)
                        .filter(corridor -> corridor.getSensor() != null)
                        .forEach(corridor -> corridor.getSensor().registerListener(this)));
    }

    public void unregisterFromSensorEvents() {
        unregisterFromSensorEvents(hotel);
    }


    public void unregisterFromSensorEvents(Hotel hotel) {
        hotel.getFloors().stream()
                .map(Floor::getCorridors)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(corridor -> corridor.getSensor() != null)
                .forEach(corridor -> corridor.getSensor().unregisterListener());
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
        corridor.getEquipments().stream()
                .forEach(equipment -> {
                    corridor.getEquipmentStack().push(equipment);
                    equipment.switchOn();
                    printInfo(floor, corridor, equipment, "Switching on ");
                });
        long difference = getPowerDifference(floor);
        if (difference > 0) {
            compensatePowerConsumption(floor, corridor, difference);
        }
    }

    private void compensatePowerConsumption(Floor floor, Corridor corridor, long power) {
        List<Corridor> corridors = floor.getCorridors();
        corridors.removeIf(corr -> corr instanceof MainCorridor); // Consider only sub corridors for compensation in night mode
        if (corridors.size() > 1) { // Do not consider the corridor in which movement was detected
            corridors.remove(corridor);
        }
        int numCorridors = corridors.size();
        while (numCorridors > 0 && power > 0) {
            Corridor chosenCorridor = getRandomCorridor(corridors, numCorridors);
            List<Equipment> equipments = chosenCorridor.getEquipments();
            sort(equipments);   //Sort in the descending order of priority
            equipments.removeIf(equipment -> equipment.getPriority() == HIGH); //Do not consider high priority equipments for compensation
            power = switchOffEquipmentsToCompensate(floor, corridor, power, chosenCorridor, equipments);
            numCorridors--;
        }
    }

    private long switchOffEquipmentsToCompensate(Floor floor, Corridor corridor, long power,
                                                 Corridor chosenCorridor, List<Equipment> equipments) {
        for (Equipment equipment : equipments) {
            corridor.getEquipmentStack().push(equipment);
            equipment.switchOff();
            printInfo(floor, chosenCorridor, equipment, "Switching off ");
            power -= equipment.getPowerUnits();
            if (power <= 0)
                break;
        }
        return power;
    }

    private Corridor getRandomCorridor(List<Corridor> corridors, int numCorridors) {
        Random random = new Random(); // Pick a random sub corridor
        int corridorIndex = random.nextInt(numCorridors);
        return corridors.get(corridorIndex);
    }

    private void handleNoMovement(Floor floor, Corridor corridor) {
        System.out.println(" No Movement detected at Floor number " + floor.getFloorNumber() + ", Corridor number " + corridor.getId());
        corridor.setMovementDetected(false);
        controlEquipmentsNoOnMovement(floor, corridor);
        printHotelInfo(hotel);
    }

    private void controlEquipmentsNoOnMovement(Floor floor, Corridor corridor) {
        revertStateChange(corridor.getEquipmentStack());
        long difference = getPowerDifference(floor);
        if (difference > 0) {
            List<Equipment> equipments = corridor.getEquipments();
            sort(equipments);
            while (difference > 0) {
                for (Equipment equipment : equipments) { // Compensate by switching off equipments in this corridor
                    equipment.switchOff();
                    printInfo(floor, corridor, equipment, "Switching off ");
                    corridor.getEquipmentStack().push(equipment);
                    difference -= equipment.getPowerUnits();
                    if (difference <= 0)
                        break;
                }
            }
        }
    }

    private long getPowerDifference(Floor floor) {
        long difference = floor.getPowerConsumption() - floor.getMaxPowerConsumption();
        if (difference > 0) {
            floor.setPowerDifference(difference);
            System.out.println("Power consumption is " + floor.getPowerConsumption() + " units");
            System.out.println("Power consumption exceeded by " + difference + " units");
        }
        return difference;
    }

    private void revertStateChange(Stack<Equipment> stateChangeTrail) {
        while (!stateChangeTrail.isEmpty()) {
            Equipment equipment = stateChangeTrail.pop();
            if (equipment.getType().equals(LIGHT)) {
                equipment.switchOff();
            } else {
                equipment.switchOn();
            }
        }
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    private void printInfo(Floor floor, Corridor corridor, Equipment equipment, String state) {
        System.out.println(state + equipment.getType() + " of Floor " + floor.getFloorNumber()
                + ", Corridor " + corridor.getId());
    }
}

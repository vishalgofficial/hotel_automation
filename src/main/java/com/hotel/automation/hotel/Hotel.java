package com.hotel.automation.hotel;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.corridors.SubCorridor;
import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.sensor.MovementSensor;
import com.hotel.automation.sensor.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hotel.automation.equipment.Type.AC;
import static com.hotel.automation.equipment.Type.LIGHT;
import static java.util.stream.IntStream.range;

public class Hotel {
    private List<Floor> floors = new ArrayList<>();

    public Hotel(int floorCount, int mainCorridorCount, int subCorridorCount) {
        createFloor(floorCount);
        createMainCorridorForEachFloor(mainCorridorCount);
        createSubCorridorForEachFloor(subCorridorCount);
        addEquipmentToEachCorridor();
        addSensors();
    }

    private void createFloor(int floorCount) {
        range(0, floorCount)
                .forEach(i -> floors.add(new Floor(i + 1)));
    }

    private void createMainCorridorForEachFloor(int mainCorridorCount) {
        floors.forEach(floor -> range(0, mainCorridorCount)
                .mapToObj(i -> new MainCorridor(i + 1))
                .forEach(floor::addCorridors));
    }

    private void createSubCorridorForEachFloor(int subCorridorCount) {
        floors.forEach(floor -> range(0, subCorridorCount)
                .mapToObj(i -> new SubCorridor(i + 1))
                .forEach(floor::addCorridors));

    }

    private void addEquipmentToEachCorridor() {
        for (Floor floor : floors) {
            List<Corridor> corridors = floor.getCorridors();
            for (Corridor corridor : corridors) {
                addEquipments(corridor);
            }
        }
    }

    private void addEquipments(Corridor corridor) {
        Equipment light = new Equipment(LIGHT, 5);
        corridor.addEquipment(light);

        Equipment ac = new Equipment(AC, 10);
        corridor.addEquipment(ac);
    }

    private void addSensors() {
        floors.forEach(f -> {
            f.getCorridors().forEach(corridor -> {
                Sensor sensor = new MovementSensor(f, corridor);
                corridor.setSensor(sensor);
            });
        });
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public Optional<Floor> getFloorByNumber(int number) {
        return floors.stream()
                .filter(floor -> floor.getFloorNumber() == number)
                .findFirst();
    }
}

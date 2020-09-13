package com.hotel.automation.corridors;

import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.sensor.Sensor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.hotel.automation.equipment.Type.AC;
import static com.hotel.automation.equipment.Type.LIGHT;
import static java.util.stream.Collectors.toList;

@Setter
@Getter
public abstract class Corridor {
    private String id;
    protected List<Equipment> equipments = new ArrayList<Equipment>();
    private Stack<Equipment> equipmentStack = new Stack<>();
    private Sensor sensor;
    private boolean movementDetected;

    public abstract void setDefaultState();

    public Corridor(String id) {
        this.id = id;
    }

    public List<Equipment> getEquipments() {
        return new ArrayList<>(equipments);
    }

    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
    }

    public int getPowerConsumption() {
        return equipments.stream().filter(Equipment::isOn)
                .map(Equipment::getPowerUnits)
                .reduce(0, Integer::sum);
    }
}

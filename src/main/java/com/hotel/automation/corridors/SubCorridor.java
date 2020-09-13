package com.hotel.automation.corridors;

import com.hotel.automation.equipment.Equipment;

import static com.hotel.automation.equipment.Type.AC;
import static com.hotel.automation.equipment.Type.LIGHT;

public class SubCorridor extends Corridor {
    public SubCorridor(int id) {
        super("SubCorridor" + id);
    }

    @Override
    public void setDefaultState() {
        getEquipments().stream()
                .filter(equipment -> equipment.getType() == AC)
                .forEach(Equipment::switchOn);
 /*       getEquipments().stream()
                .filter(equipment -> equipment.getType() == LIGHT)
                .forEach(Equipment::switchOff);*/
    }
}

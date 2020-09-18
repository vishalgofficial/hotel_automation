package com.hotel.automation.corridors;

import com.hotel.automation.equipment.Equipment;

import static com.hotel.automation.equipment.Type.AC;

public class SubCorridor extends Corridor {
    public SubCorridor(int id) {
        super("SubCorridor" + id);
    }

    @Override
    public void setDefaultState() {
        getEquipments().stream()
                .filter(equipment -> equipment.getType() == AC)
                .forEach(Equipment::switchOn);
    }
}

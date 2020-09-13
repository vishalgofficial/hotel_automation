package com.hotel.automation.corridors;

import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.equipment.Type;

public class MainCorridor extends Corridor {

    public MainCorridor(int id) {
        super("MainCorridor" + id);
    }

    @Override
    public void setDefaultState() {
        setDefaultStateOfEquipments(Type.AC);
        setDefaultStateOfEquipments(Type.LIGHT);
    }

    private void setDefaultStateOfEquipments(Type type) {
        getEquipments().stream()
                .filter(equipment -> equipment.getType() == type)
                .forEach(Equipment::switchOn);
    }
}

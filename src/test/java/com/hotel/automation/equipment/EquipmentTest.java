package com.hotel.automation.equipment;

import com.hotel.automation.controller.HotelController;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.hotel.automation.equipment.State.OFF;
import static com.hotel.automation.equipment.State.ON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquipmentTest {

    @Test
    public void testLightActionOn() {
        Equipment light = new Equipment(Type.LIGHT, 5);
        light.switchOn();
        assertThat(light.getState()).isEqualTo(ON);
    }

    @Test
    public void testLightActionOff() {
        Equipment light = new Equipment(Type.LIGHT, 5);
        light.switchOff();
        assertThat(light.getState()).isEqualTo(OFF);
    }

    @Test
    public void testACActionOn() {
        Equipment ac = new Equipment(Type.AC, 10);
        ac.switchOn();
        assertThat(ac.getState()).isEqualTo(ON);
    }

    @Test
    public void testACActionOff() {
        Equipment ac = new Equipment(Type.AC, 10);
        ac.switchOff();
        assertThat(ac.getState()).isEqualTo(OFF);
    }

    @Test
    public void testMainCorridorLightsInitialState() {
        HotelController controller = new HotelController(1, 2, 2);
        List<Equipment> equipmentList = controller.getHotel().getFloors().get(0).getCorridors().get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType().equals(Type.LIGHT)).collect(Collectors.toList());
        assertTrue(equipmentList.get(0).isOn());
    }

    @Test
    public void testCorridorACsInitialState() {
        HotelController controller = new HotelController(1, 2, 2);
        List<Equipment> equipmentList = controller.getHotel().getFloors().get(0).getCorridors().get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType().equals(Type.AC)).collect(Collectors.toList());
        assertTrue(equipmentList.get(0).isOn());
    }
}
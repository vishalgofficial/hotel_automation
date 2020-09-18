package com.hotel.automation.hotel;

import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.corridors.SubCorridor;
import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.equipment.Type;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertEquals;

class FloorTest {
    @Test
    public void testCreateFloor() {
        Floor floor = new Floor(3);
        assertEquals("true", floor.getFloorNumber(), 3);
    }

    @Test
    public void testCreateFloorWithMainCorridor() {
        Floor floor = new Floor(1);
        MainCorridor mc = new MainCorridor(1);
        floor.addCorridors(mc);
        assertEquals("true", floor.getMainCorridorsSize(), 1);
    }

    @Test
    public void testCreateFloorWithSubCorridor() {
        Floor floor = new Floor(1);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridors(sc);
        assertEquals("true", floor.getSubCorridorsSize(), 1);
    }

    @Test
    public void testGetFloorMaxPowerConsumption() {
        Floor floor = new Floor(1);
        MainCorridor mc = new MainCorridor(1);
        floor.addCorridors(mc);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridors(sc);
        //Max power consumption should be (mc * 15) + (sc * 10)
        assertEquals("true",(int) floor.getMaxPowerConsumption(), 25);
    }

    @Test
    public void testFloorPowerConsumption() {
        Floor floor = new Floor(1);
        MainCorridor mc = new MainCorridor(1);
        floor.addCorridors(mc);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridors(sc);

        Equipment light1 = new Equipment(Type.LIGHT, 5);
        light1.switchOn();
        mc.addEquipment(light1);
        Equipment ac1 = new Equipment(Type.AC, 10);
        ac1.switchOn();
        mc.addEquipment(ac1);

        Equipment light2 = new Equipment(Type.LIGHT, 5);
        light2.switchOff();
        sc.addEquipment(light2);
        Equipment ac2 = new Equipment(Type.AC, 10);
        ac2.switchOn();
        sc.addEquipment(ac2);

        assertEquals("true", (int)floor.getPowerConsumption(), 25);
    }

}
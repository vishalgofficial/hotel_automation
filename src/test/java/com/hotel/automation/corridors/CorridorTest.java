package com.hotel.automation.corridors;

import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.equipment.Type;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.sensor.MovementSensor;
import com.hotel.automation.sensor.Sensor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class CorridorTest {
    @Test
    public void testCreateMainCorridorWithLight() {
        MainCorridor mc = new MainCorridor(1);
        Equipment light = new Equipment(Type.LIGHT, 5);
        mc.addEquipment(light);
        assertEquals("true", mc.getEquipments().get(0).getType(), Type.LIGHT);
    }

    @Test
    public void testCreateMainCorridorWithAC() {
        MainCorridor mc = new MainCorridor(1);
        Equipment light = new Equipment(Type.AC, 10);
        mc.addEquipment(light);

        assertEquals("true", mc.getEquipments().get(0).getType(), Type.AC);
    }

    @Test
    public void testCreateSubCorridorWithLight() {
        SubCorridor sc = new SubCorridor(1);
        Equipment light = new Equipment(Type.LIGHT, 5);
        sc.addEquipment(light);

        assertEquals("true", sc.getEquipments().get(0).getType(), Type.LIGHT);
    }

    @Test
    public void testCreateSubCorridorWithAC() {
        SubCorridor sc = new SubCorridor(1);
        Equipment light = new Equipment(Type.AC, 10);
        sc.addEquipment(light);

        assertEquals("true", sc.getEquipments().get(0).getType(), Type.AC);
    }

    @Test
    public void testMainCorridorPowerConsumption() {
        MainCorridor mc = new MainCorridor(1);

        Equipment light = new Equipment(Type.LIGHT, 5);
        light.switchOff();
        Equipment ac = new Equipment(Type.AC, 10);
        ac.switchOn();

        mc.addEquipment(light);
        mc.addEquipment(ac);

        assertEquals("true", mc.getPowerConsumption(), 10);
    }

    @Test
    public void testSubCorridorPowerConsumption() {
        SubCorridor sc = new SubCorridor(1);

        Equipment light = new Equipment(Type.LIGHT, 5);
        light.switchOn();
        Equipment ac = new Equipment(Type.AC, 10);
        ac.switchOff();

        sc.addEquipment(light);
        sc.addEquipment(ac);

        assertEquals("true", sc.getPowerConsumption(), 5);
    }

    @Test
    public void testCreateCorridorWithSensor() {
        Floor floor = new Floor(1);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridors(sc);
        Sensor sensor = new MovementSensor(floor, sc);
        assertTrue(sensor.getId().startsWith("1-SubCorridor1"));
    }

}
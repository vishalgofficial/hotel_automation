package com.hotel.automation.equipment;

import org.junit.jupiter.api.Test;

import static com.hotel.automation.equipment.State.OFF;
import static com.hotel.automation.equipment.State.ON;
import static org.assertj.core.api.Assertions.assertThat;

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

}
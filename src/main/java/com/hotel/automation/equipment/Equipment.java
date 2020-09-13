package com.hotel.automation.equipment;

import lombok.Getter;
import lombok.Setter;

import static com.hotel.automation.equipment.State.OFF;
import static com.hotel.automation.equipment.State.ON;

@Getter
@Setter
public class Equipment {

    private Type type;
    private int powerUnits;
    private State state = OFF;

    public Equipment(Type type, int powerUnit) {
        this.type = type;
        this.powerUnits = powerUnit;
    }

    public void switchOn() {
        this.state = ON;
    }

    public void switchOff() {
        this.state = OFF;
        this.powerUnits = 0;
    }

    public boolean isOn() {
        return this.state == State.ON;
    }

    public boolean isOff() {
        return this.state == State.OFF;
    }


}

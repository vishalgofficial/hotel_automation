package com.hotel.automation.equipment;

import lombok.Getter;
import lombok.Setter;

import static com.hotel.automation.equipment.Priority.LOW;
import static com.hotel.automation.equipment.State.OFF;
import static com.hotel.automation.equipment.State.ON;

@Getter
@Setter
public class Equipment implements Comparable<Equipment> {

    private Type type;
    private int powerUnits;
    private State state = OFF;
    private Priority priority = LOW;

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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /*Sorts the equipments in the descending order of priority */
    @Override
    public int compareTo(Equipment equipment) {
        return equipment.getPriority().getPriorityNumber() - this.getPriority().getPriorityNumber();
    }
}

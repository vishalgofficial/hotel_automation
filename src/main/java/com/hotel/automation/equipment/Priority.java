package com.hotel.automation.equipment;

/**
 * Priority of an equipment
 */

public enum Priority {
    HIGH(1),
    LOW(2);

    private int priorityNumber;

    Priority(int priorityNumber){
        this.priorityNumber = priorityNumber;
    }

    public int getPriorityNumber(){
        return this.priorityNumber;
    }
}

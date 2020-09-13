package com.hotel.automation.listener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {

    private int floorNumber;
    private String corridorId;
    private boolean movementDetected;

}

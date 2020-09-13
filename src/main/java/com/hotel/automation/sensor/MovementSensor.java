package com.hotel.automation.sensor;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.hotel.Floor;

public class MovementSensor extends Sensor {
    public MovementSensor(Floor floor, Corridor corridor) {
        super(floor, corridor);
    }

    public void eventDetected(boolean detected) {
        this.notifyListener(detected);
    }
}

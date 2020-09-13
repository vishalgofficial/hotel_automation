package com.hotel.automation.util;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.hotel.Floor;

import java.util.Random;

public class SensorUtil {

    public static String generateSensorId(Floor floor, Corridor corridor) {
        String id = floor.getFloorNumber() + "-" + corridor.getId();
        Random random = new Random();
        id = id + ("-" + random.nextInt(1000));
        return id;
    }
}

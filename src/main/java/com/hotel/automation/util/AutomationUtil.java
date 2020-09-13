package com.hotel.automation.util;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.hotel.Hotel;

import static java.util.Optional.of;

public class AutomationUtil {

    public static Floor getFloorFromSensorId(String sensorId, Hotel hotel) {
        String[] sensorIdParts = sensorId.split("-");
        int floorNumber = Integer.parseInt(sensorIdParts[0]);
        return of(hotel.getFloors()
                .stream()
                .filter(floor -> floor.getFloorNumber() == floorNumber)
                .findFirst().get())
                .orElseThrow(() -> new RuntimeException("Floor with number " + floorNumber + " not found"));
    }

    public static Corridor getCorridorFromSensorId(String sensorId, Floor floor) {
        String[] sensorIdParts = sensorId.split("-");
        String corridorId = sensorIdParts[1];
        return of(floor.getCorridors()
                .stream()
                .filter(corridor -> corridor.getId().equalsIgnoreCase(corridorId))
                .findFirst().get())
                .orElseThrow(() -> new RuntimeException("Corridor with id " + corridorId + " not found"));
    }
}

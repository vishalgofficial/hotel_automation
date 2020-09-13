package com.hotel.automation.util;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.corridors.SubCorridor;
import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.hotel.Hotel;

import java.util.List;
import java.util.stream.Collectors;

import static com.hotel.automation.equipment.Type.AC;
import static com.hotel.automation.equipment.Type.LIGHT;
import static java.util.stream.Collectors.toList;

public class PrintUtil {

    public static void printHotelInfo(Hotel hotel) {
        hotel.getFloors().forEach(floor -> printFloorInfo(floor));
        System.out.println(">>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<");
    }

    private static void printFloorInfo(Floor floor) {
        System.out.println("              Floor " + floor.getFloorNumber());
        List<Corridor> corridors = floor.getCorridors();
        List<Corridor> mainCorridors = corridors.stream()
                .filter(corridor -> corridor instanceof MainCorridor)
                .collect(Collectors.toList());
        List<Corridor> subCorridors = corridors.stream()
                .filter(corridor -> corridor instanceof SubCorridor)
                .collect(Collectors.toList());
        mainCorridors.forEach(mainCorridor -> {
            System.out.print("Main corridor " + (mainCorridors.indexOf(mainCorridor) + 1));
            printCorridorInfo(mainCorridor);
            System.out.println();
        });

        subCorridors.forEach(subCorridor -> {
            System.out.print("Sub corridor " + (subCorridors.indexOf(subCorridor) + 1));
            printCorridorInfo(subCorridor);
            System.out.println();
        });

        System.out.println("Power consumption is " + floor.getPowerConsumption() + " units");
    }

    public static void printCorridorInfo(Corridor corridor) {
        List<Equipment> lights = corridor.getEquipments().stream()
                .filter(equipment -> equipment.getType() == LIGHT).collect(toList());
        List<Equipment> acs = corridor.getEquipments().stream()
                .filter(equipment -> equipment.getType() == AC).collect(toList());

        lights.forEach(light -> {
            System.out.print(" " + light.getType() + " " + (lights.indexOf(light) + 1) + " : " + light.getState());
        });
        acs.forEach(ac -> {
            System.out.print(" " + ac.getType() + " " + (acs.indexOf(ac) + 1) + " : " + ac.getState());
        });
    }
}

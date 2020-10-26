package com.hotel.automation.util;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.corridors.SubCorridor;
import com.hotel.automation.equipment.Equipment;
import com.hotel.automation.equipment.Type;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.hotel.Hotel;

import java.util.List;
import java.util.stream.Collectors;

import static com.hotel.automation.equipment.Type.AC;
import static com.hotel.automation.equipment.Type.LIGHT;
import static java.util.stream.Collectors.toList;

public class PrintUtil {

    private static final String MAIN_CORRIDOR_ = "Main corridor ";
    private static final String SUB_CORRIDOR = "Sub corridor ";

    public static void printHotelInfo(Hotel hotel) {
        hotel.getFloors().forEach(PrintUtil::printFloorInfo);
        System.out.println(">>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<");
    }

    private static void printFloorInfo(Floor floor) {
        System.out.println("              Floor " + floor.getFloorNumber());
        List<Corridor> corridors = floor.getCorridors();
        printCorridorInfo(extractMainCorridors(corridors), MAIN_CORRIDOR_);
        printCorridorInfo(extractSubCorridors(corridors), SUB_CORRIDOR);
        System.out.println("Power consumption is " + floor.getPowerConsumption() + " units");
    }

    private static void printCorridorInfo(List<Corridor> corridors, String typeOfCorridor) {
        corridors.forEach(mainCorridor -> {
            System.out.print(typeOfCorridor + (corridors.indexOf(mainCorridor) + 1));
            printCorridorInfo(mainCorridor);
            System.out.println();
        });
    }

    private static List<Corridor> extractSubCorridors(List<Corridor> corridors) {
        return corridors.stream()
                .filter(corridor -> corridor instanceof SubCorridor)
                .collect(Collectors.toList());
    }

    private static List<Corridor> extractMainCorridors(List<Corridor> corridors) {
        return corridors.stream()
                .filter(corridor -> corridor instanceof MainCorridor)
                .collect(Collectors.toList());
    }

    public static void printCorridorInfo(Corridor corridor) {
        showEquipmentStateDetails(getEquipmentLight(corridor, LIGHT));
        showEquipmentStateDetails(getEquipmentLight(corridor, AC));
    }

    private static List<Equipment> getEquipmentLight(Corridor corridor, Type equipmentType) {
        return corridor.getEquipments().stream()
                .filter(equipment -> equipment.getType() == equipmentType).collect(toList());
    }

    private static void showEquipmentStateDetails(List<Equipment> equipmentList) {
        equipmentList.forEach(light -> {
            System.out.print(" " + light.getType() + " " + (equipmentList.indexOf(light) + 1) + " : " + light.getState());
        });
    }
}

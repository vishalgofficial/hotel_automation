package com.hotel.automation.hotel;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.corridors.SubCorridor;
import com.hotel.automation.equipment.Equipment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.hotel.automation.equipment.Type.AC;
import static com.hotel.automation.equipment.Type.LIGHT;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class HotelTest {

    @Test
    void hotelCanHaveMultipleFloors() {
        Hotel hotel = new Hotel(2, 0, 0);
        assertThat(hotel.getFloors().size()).isEqualTo(2);
    }

    @Test
    void eachFloorCanHaveMultipleMainCorridors() {
        Hotel hotel = new Hotel(2, 3, 0);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof MainCorridor).collect(toList());
        assertThat(corridors.size()).isEqualTo(3);
        corridors.forEach(c -> assertThat(c).isInstanceOf(MainCorridor.class));
    }

    @Test
    void eachFloorCanHaveMultipleSubCorridors() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof SubCorridor).collect(toList());
        assertThat(corridors.size()).isEqualTo(3);
        corridors.forEach(c -> assertThat(c).isInstanceOf(SubCorridor.class));
    }

    @Test
    void lightConsumes5Units() {
        assertThat(new Equipment(LIGHT, 5).getPowerUnits()).isEqualTo(5);
    }

    @Test
    void lightConsumes0UnitsWhenOff() {
        Equipment equipment = new Equipment(LIGHT, 5);
        equipment.switchOff();
        assertThat(equipment.getPowerUnits()).isEqualTo(0);
    }

    @Test
    void eachCorridorsHasOneLight() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof MainCorridor).collect(toList());
        List<Equipment> lightEquipments = corridors.get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType() == LIGHT).collect(toList());
        assertThat(lightEquipments.size()).isEqualTo(1);
    }

    @Test
    void eachSubCorridorsHasOneLight() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof SubCorridor).collect(toList());
        List<Equipment> lightEquipments = corridors.get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType() == LIGHT).collect(toList());
        assertThat(lightEquipments.size()).isEqualTo(1);
    }

    @Test
    void acConsumes10Units() {
        assertThat(new Equipment(AC, 10).getPowerUnits()).isEqualTo(10);
    }

    @Test
    void acConsumes0UnitsWhenOff() {
        Equipment equipment = new Equipment(AC, 10);
        equipment.switchOff();
        assertThat(equipment.getPowerUnits()).isEqualTo(0);
    }

    @Test
    void eachCorridorsHasOneAc() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof MainCorridor).collect(toList());
        List<Equipment> lightEquipments = corridors.get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType() == AC).collect(toList());
        assertThat(lightEquipments.size()).isEqualTo(1);
    }

    @Test
    void eachSubCorridorsHasOneAc() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof SubCorridor).collect(toList());
        List<Equipment> lightEquipments = corridors.get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType() == AC).collect(toList());
        assertThat(lightEquipments.size()).isEqualTo(1);
    }

    @Test
    void mainCorridorHasAllTheLightsOnByDefault() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof MainCorridor).collect(toList());
        corridors.forEach(Corridor::setDefaultState);
        List<Equipment> lightEquipments = corridors.get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType() == LIGHT).collect(toList());
        lightEquipments.forEach(l -> assertThat(l.isOn()).isTrue());
    }

    @Test
    void subCorridorHasAllTheListsOffByDefault() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof SubCorridor).collect(toList());
        List<Equipment> lightEquipments = corridors.get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType() == LIGHT).collect(toList());
        lightEquipments.forEach(l -> assertThat(l.isOn()).isFalse());
    }

    @Test
    void mainCorridorHasAllTheAcsOnByDefault() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof MainCorridor).collect(toList());
        corridors.forEach(Corridor::setDefaultState);
        List<Equipment> lightEquipments = corridors.get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType() == AC).collect(toList());
        lightEquipments.forEach(l -> assertThat(l.isOn()).isTrue());
    }

    @Test
    void subCorridorHasAllTheAcsOnByDefault() {
        Hotel hotel = new Hotel(2, 3, 3);
        List<Corridor> corridors = hotel.getFloors().get(0).getCorridors().stream()
                .filter(corridor -> corridor instanceof SubCorridor).collect(toList());
        corridors.forEach(Corridor::setDefaultState);
        List<Equipment> lightEquipments = corridors.get(0).getEquipments()
                .stream().filter(equipment -> equipment.getType() == AC).collect(toList());
        lightEquipments.forEach(l -> assertThat(l.isOn()).isTrue());
    }
}
package com.hotel.automation.hotel;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.corridors.MainCorridor;
import com.hotel.automation.corridors.SubCorridor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class Floor {
    private int floorNumber;
    private List<Corridor> corridors = new ArrayList<>();

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public List<Corridor> getCorridors() {
        return new ArrayList<>(this.corridors);
    }

    public void addCorridors(Corridor corridors) {
        this.corridors.add(corridors);
    }

    public long getPowerConsumption() {
        return corridors.stream().map(Corridor::getPowerConsumption)
                .reduce(0, Integer::sum);
    }

    public Optional<Corridor> getCorridorById(String id) {
        return corridors.stream()
                .filter(corridor -> corridor.getId().equals(id))
                .findFirst();
    }

    public long getMaxPowerConsumption() {
        return getMainCorridorsSize() * 15 + getSubCorridorsSize() * 10;
    }

    public long getMainCorridorsSize() {
        return corridors.stream().filter(corridor -> corridor instanceof MainCorridor).count();
    }

    public long getSubCorridorsSize() {
        return corridors.stream().filter(corridor -> corridor instanceof SubCorridor).count();
    }
}
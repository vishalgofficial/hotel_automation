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
    private long powerDifference;
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

    public int getMainCorridorsSize() {
        return (int) corridors.stream().filter(corridor -> corridor instanceof MainCorridor).count();
    }

    public int getSubCorridorsSize() {
        return (int) corridors.stream().filter(corridor -> corridor instanceof SubCorridor).count();
    }

    public long getPowerDifference() {
        return powerDifference;
    }

    public void setPowerDifference(long powerDifference) {
        this.powerDifference = powerDifference;
    }
}

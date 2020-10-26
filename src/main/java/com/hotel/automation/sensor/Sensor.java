package com.hotel.automation.sensor;

import com.hotel.automation.corridors.Corridor;
import com.hotel.automation.hotel.Floor;
import com.hotel.automation.listener.EventListener;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

import static com.hotel.automation.util.SensorUtil.generateSensorId;

@Setter
@Getter
public abstract class Sensor {
    private String id;
    private EventListener eventListener;

    public Sensor(Floor floor, Corridor corridor) {
        this.id = generateSensorId(floor, corridor);
    }

    public abstract void eventDetected(boolean detected);

    public void registerListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void notifyListener(boolean detected) {
        Optional.of(eventListener)
                .ifPresent(eventListener -> {
                    try {
                        eventListener.onEventDetected(this.getId(), detected);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void unregisterListener() {
        this.eventListener = null;
    }

}

package com.hotel.automation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.automation.controller.HotelController;
import com.hotel.automation.util.EventHandler;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.io.FileUtils.readFileToString;

public class FunctionalTest {
    private HotelController hotelController;

    @Test
    void scenarioFirst() {
        EventHandler eventHandler = readSettingsFromFile("input1.json");
        executeUseCase(eventHandler);
        hotelController.actOnMovementEvent(eventHandler.getEvents());
    }

    @Test
    void scenarioSecond() {
        EventHandler eventHandler = readSettingsFromFile("input2.json");
        executeUseCase(eventHandler);
        hotelController.actOnMovementEvent(eventHandler.getEvents());
    }

    @Test
    void scenarioThird() {
        EventHandler eventHandler = readSettingsFromFile("input3.json");
        executeUseCase(eventHandler);
        hotelController.actOnMovementEvent(eventHandler.getEvents());
    }

    private void executeUseCase(EventHandler handler) {
        hotelController = new HotelController(handler.getNumFloors(), handler.getNumMainCorridors(), handler.getNumSubCorridors());
    }

    private EventHandler readSettingsFromFile(String inputFileName) {
        EventHandler eventHandler = new EventHandler();
        try {
            ClassLoader classLoader = FunctionalTest.class.getClassLoader();
            URL fileUrl = classLoader.getResource(inputFileName);
            if (fileUrl != null) {
                File inputFile = new File(fileUrl.getFile());
                String inputJson = readFileToString(inputFile, StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();
                eventHandler = mapper.readValue(inputJson, EventHandler.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventHandler;
    }

}

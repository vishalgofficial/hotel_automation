package com.hotel.automation.controller;

import com.hotel.automation.hotel.HotelRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelAutomationRestController {

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public void operate(@RequestBody HotelRequest hotelRequest) {
        HotelController hotelController = new HotelController(hotelRequest);
        hotelController.actOnMovementEvent(hotelRequest.getEvents());
    }

    /* Sample input file present under resource folder*/
}

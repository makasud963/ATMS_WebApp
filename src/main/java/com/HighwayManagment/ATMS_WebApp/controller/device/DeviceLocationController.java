package com.HighwayManagment.ATMS_WebApp.controller.device;

import com.HighwayManagment.ATMS_WebApp.dto.device.DeviceLocationDTO;
import com.HighwayManagment.ATMS_WebApp.service.device.DeviceLocationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device-locations")
public class DeviceLocationController {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    DeviceLocationController.class);

    @Autowired
    private DeviceLocationService locationService;

    @PostMapping("/create")
    public ResponseEntity<String> createLocation(
            @Valid @RequestBody DeviceLocationDTO dto) {

        logger.info(
                "Received create location request"
        );

        return locationService.createLocation(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DeviceLocationDTO>>
    getAllLocations() {

        logger.info(
                "Received request to fetch all locations"
        );

        return ResponseEntity.ok(
                locationService.getAllLocations()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceLocationDTO>
    getLocationById(@PathVariable Long id) {

        logger.info(
                "Received request for location Id={}",
                id
        );

        return ResponseEntity.ok(
                locationService.getLocationById(id)
        );
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<DeviceLocationDTO>
    getLocationByDeviceId(
            @PathVariable Long deviceId) {

        logger.info(
                "Received request for DeviceId={}",
                deviceId
        );

        return ResponseEntity.ok(
                locationService.getLocationByDeviceId(
                        deviceId
                )
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody DeviceLocationDTO dto) {

        return locationService.updateLocation(
                id,
                dto
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLocation(
            @PathVariable Long id) {

        return locationService.deleteLocation(id);
    }
}

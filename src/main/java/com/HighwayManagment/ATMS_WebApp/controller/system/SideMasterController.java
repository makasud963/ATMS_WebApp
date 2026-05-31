package com.HighwayManagment.ATMS_WebApp.controller.system;

import com.HighwayManagment.ATMS_WebApp.dto.system.SideMasterDTO;
import com.HighwayManagment.ATMS_WebApp.service.system.SideMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sides")
public class SideMasterController {

    @Autowired
    private SideMasterService sideService;

    @PostMapping("/create")
    public ResponseEntity<String> createSide(@RequestBody SideMasterDTO dto) {
        return sideService.createSide(dto);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SideMasterDTO>> getAllActiveSides() {
        List<SideMasterDTO> sides = sideService.getAllActiveSides();
        return ResponseEntity.ok(sides);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SideMasterDTO> getSideById(@PathVariable Integer id) {
        SideMasterDTO side = sideService.getSideById(id);
        return ResponseEntity.ok(side);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSide(@PathVariable Integer id, @RequestBody SideMasterDTO dto) {
        return sideService.updateSide(id, dto);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteSide(@PathVariable Integer id) {
        return sideService.deleteSide(id);
    }
}


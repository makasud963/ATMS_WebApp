package com.HighwayManagment.ATMS_WebApp.service.system;

import com.HighwayManagment.ATMS_WebApp.dto.system.SideMasterDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface SideMasterService {

    ResponseEntity<String> createSide(SideMasterDTO dto);

    List<SideMasterDTO> getAllActiveSides();

    ResponseEntity<String> deleteSide(Integer sideId);

    SideMasterDTO getSideById(Integer sideId);

    ResponseEntity<String> updateSide(Integer sideId, SideMasterDTO dto);
}

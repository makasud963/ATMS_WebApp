package com.HighwayManagment.ATMS_WebApp.service.system;

import com.HighwayManagment.ATMS_WebApp.dto.system.FirmMasterDTO;

import java.util.List;

public interface FirmMasterService {
    FirmMasterDTO createFirm(FirmMasterDTO dto);
    List<FirmMasterDTO> getAllFirms();
    FirmMasterDTO getFirmById(Integer id);
    FirmMasterDTO updateFirm(Integer id, FirmMasterDTO dto);
    void softDeleteFirm(Integer id, String deletedBy);
}

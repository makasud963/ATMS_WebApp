package com.HighwayManagment.ATMS_WebApp.repository.system;

import com.HighwayManagment.ATMS_WebApp.entity.system.FirmMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FirmMasterRepository extends JpaRepository<FirmMaster, Integer> {
    List<FirmMaster> findByFirmDeleteStatusFalse();
}
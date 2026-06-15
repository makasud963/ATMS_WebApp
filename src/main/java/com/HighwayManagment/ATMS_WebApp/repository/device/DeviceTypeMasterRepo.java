package com.HighwayManagment.ATMS_WebApp.repository.device;

import com.HighwayManagment.ATMS_WebApp.entity.device.DeviceTypeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTypeMasterRepo extends JpaRepository<DeviceTypeMaster, Long> {

    boolean existsByTypeName(String typeName);

    List<DeviceTypeMaster> findByDeleteStatusFalse();

    Optional<DeviceTypeMaster> findByTypeIdAndDeleteStatusFalse(Long typeId);
}
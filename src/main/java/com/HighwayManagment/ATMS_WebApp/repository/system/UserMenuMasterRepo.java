package com.HighwayManagment.ATMS_WebApp.repository.system;

import com.HighwayManagment.ATMS_WebApp.entity.system.UserMenuMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMenuMasterRepo extends JpaRepository<UserMenuMaster, Long> {

}

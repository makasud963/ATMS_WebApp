package com.HighwayManagment.ATMS_WebApp.service.system;

import com.HighwayManagment.ATMS_WebApp.dto.system.UserMenuMasterDTO;
import com.HighwayManagment.ATMS_WebApp.entity.system.UserMenuMaster;

import java.util.List;

public interface UserMenuMasterService {
    UserMenuMaster saveMenu(UserMenuMaster menu);
    UserMenuMaster updateMenu(Long id, UserMenuMaster menuDetails);
    void DeleteStatus(Long id);
    List<UserMenuMasterDTO> getAllMenus();
}

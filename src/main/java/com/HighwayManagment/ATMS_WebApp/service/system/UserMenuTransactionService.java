package com.HighwayManagment.ATMS_WebApp.service.system;

import com.HighwayManagment.ATMS_WebApp.dto.system.FlatMenuPermission;
import com.HighwayManagment.ATMS_WebApp.dto.system.UserMenuTransactionDTO;
import com.HighwayManagment.ATMS_WebApp.entity.enumData.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserMenuTransactionService {

    ResponseEntity<String> assignPermission(UserMenuTransactionDTO dto);

    List<UserMenuTransactionDTO> getUserPermissionsByRole(UserRole userRole);

    //List<UserMenuTransactionDTO> getUserPermissionsFlat(UserRole userRole);
    List<FlatMenuPermission> getUserPermissionsFlat(UserRole userRole);

    void deleteMenuTrns(Long id);

    ResponseEntity<String> assignPermissionsBulk(List<UserMenuTransactionDTO> dtoList);
}
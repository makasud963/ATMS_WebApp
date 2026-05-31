package com.HighwayManagment.ATMS_WebApp.dto.system;


import com.HighwayManagment.ATMS_WebApp.entity.enumData.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuTransactionDTO {

    private Long menuTrnId;
    private UserRole userRole;
    private Long menuId;
    private Boolean canAdd;
    private Boolean canUpdate;
    private Boolean canDelete;
    private Boolean canDisplay;

    private LocalDateTime assignedAt;
    private String assignedBy;

    private Boolean isActive;

    private String menuName;
    private String menuUrl;
    private String icon;
    private Long parentId;

    private List<UserMenuTransactionDTO> children = new ArrayList<>();
}
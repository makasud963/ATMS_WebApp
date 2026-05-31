package com.HighwayManagment.ATMS_WebApp.dto.system;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlatMenuPermission {
    private Long menuTrnId;
    private String userRole;
    private Long menuId;
    private Boolean canAdd;
    private Boolean canUpdate;
    private Boolean canDelete;
    private Boolean canDisplay;
    private Boolean isActive;
    private String menuName;
    private String menuUrl;
    private String icon;
    private Long parentId;
}

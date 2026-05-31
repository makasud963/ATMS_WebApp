package com.HighwayManagment.ATMS_WebApp.dto.system;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserMenuMasterDTO {
    private Long menuId;
    private String menuName;
    private String menuUrl;
    private String icon;
    private Long parentId;
    private Boolean isActive;

    private String mainMenu;       // Parent menu name
    private String subMenu;        // Translated menu name
    private String url;            // menuUrl
    private String iconUrl;        // icon image URL
    private String active;         // Active/Inactive

    private List<UserMenuMasterDTO> children = new ArrayList<>();
}

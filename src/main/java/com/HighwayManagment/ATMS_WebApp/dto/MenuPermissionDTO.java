package com.HighwayManagment.ATMS_WebApp.dto;
import lombok.Data;
@Data
public class MenuPermissionDTO {

    private String menuUrl;

    private Boolean canAdd;

    private Boolean canUpdate;

    private Boolean canDelete;

    private Boolean canDisplay;
}
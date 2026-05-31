package com.HighwayManagment.ATMS_WebApp.dto.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmMasterDTO {
    private Integer firmId;
    private String firmName;
    private String firmNameRegLang;
    private String firmRegNumber;
    private Date firmRegDate;
    private String firmOfficeAddress;
    private String firmOfficeAddressRegLang;
    private String firmLogo;
    private String firmPhoneNumber;
    private String firmEmail;
    private Integer firmUserLimit;
    private boolean firmDeleteStatus;
}


package com.HighwayManagment.ATMS_WebApp.dto.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SideMasterDTO {

    private Integer sideId;
    private String sideName;
    private String sidePhoneNumber;
    private String sideEmailId;
    private Integer firmId;
    private boolean sideDeleteStatus;
}

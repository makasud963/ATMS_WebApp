package com.HighwayManagment.ATMS_WebApp.entity.system;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmMaster{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer firmId;
    @Column(nullable = false)
    private String firmName;
    private String firmNameRegLang;
    @Column(nullable = false)
    private String firmRegNumber;
    @Column(nullable = false)
    private Date firmRegDate;
    @Column(nullable = false)
    private String firmOfficeAddress;
    private String firmOfficeAddressRegLang;
    private String firmLogo;
    private String firmPhoneNumber;
    private String firmEmail;
    private Integer firmUserLimit;
    @Column(nullable = false)
    private boolean firmDeleteStatus = false;
}

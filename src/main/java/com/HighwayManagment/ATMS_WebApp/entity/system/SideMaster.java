package com.HighwayManagment.ATMS_WebApp.entity.system;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(
        name = "SideMaster",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"SideName"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SideMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SideId")
    private Integer sideId;

    @Column(name = "SideName", nullable = false)
    private String sideName;

    @Column(name = "SidePhoneNumber")
    private String sidePhoneNumber;

    @Column(name = "SideEmailIid")
    private String sideEmailIid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SideParentBranchId", nullable = false)
    private FirmMaster firmId;

    @Column(name = "SideDeleteStatus", nullable = false)
    private boolean sideDeleteStatus = false;
}

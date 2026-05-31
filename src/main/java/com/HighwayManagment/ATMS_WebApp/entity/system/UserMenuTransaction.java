package com.HighwayManagment.ATMS_WebApp.entity.system;
import com.HighwayManagment.ATMS_WebApp.entity.enumData.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_menu_transaction",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_role", "menu_id"})
        })
public class UserMenuTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuTrnId;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private UserMenuMaster menu;

    @Column(name = "can_add")
    private Boolean canAdd;

    @Column(name = "can_update")
    private Boolean canUpdate;

    @Column(name = "can_delete")
    private Boolean canDelete;

    @Column(name = "can_display")
    private Boolean canDisplay;

    @Column(name = "is_active")
    private Boolean isActive;
}

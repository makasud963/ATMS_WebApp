package com.HighwayManagment.ATMS_WebApp.entity.system;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_menu_master")
public class UserMenuMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;
    @Column(name = "menu_name", nullable = false)
    private String menuName;
    @Column(name = "menu_url", nullable = false)
    private String menuUrl;
    @Column(name = "icon")
    private String icon;
    @Column(name = "parent_id", nullable = false)
    private Long parentId;
    @Column(name = "is_active")
    private Boolean isActive;
}

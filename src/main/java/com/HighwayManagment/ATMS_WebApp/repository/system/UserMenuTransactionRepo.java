package com.HighwayManagment.ATMS_WebApp.repository.system;
import com.HighwayManagment.ATMS_WebApp.entity.enumData.UserRole;
import com.HighwayManagment.ATMS_WebApp.entity.system.UserMenuMaster;
import com.HighwayManagment.ATMS_WebApp.entity.system.UserMenuTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMenuTransactionRepo extends JpaRepository<UserMenuTransaction, Long> {

    List<UserMenuTransaction> findByUserRole(UserRole userRole);
    List<UserMenuTransaction> findByUserRoleAndIsActiveTrue(UserRole userRole);

    boolean existsByUserRoleAndMenu(
            UserRole userRole,
            UserMenuMaster menu
    );

    Optional<UserMenuTransaction> findByUserRoleAndMenu(
            UserRole userRole,
            UserMenuMaster menu
    );
}

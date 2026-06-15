package com.HighwayManagment.ATMS_WebApp.repository;

import com.HighwayManagment.ATMS_WebApp.entity.UserMaster;
import com.HighwayManagment.ATMS_WebApp.entity.system.SideMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserMaster, Long> {

    Optional<UserMaster> findByEmail(String userEmail);
    Optional<UserMaster> getRoleByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String userName);
    List<UserMaster> findByActiveTrue();

    boolean existsByUserNameIgnoreCaseAndUserIdNot(String userName, Long userId);
    boolean existsByEmailIgnoreCaseAndUserIdNot(String email, Long userId);
}
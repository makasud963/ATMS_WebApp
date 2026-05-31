package com.HighwayManagment.ATMS_WebApp.repository;

import com.HighwayManagment.ATMS_WebApp.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserMaster, Long> {

    Optional<UserMaster> findByEmail(String username);
    Optional<UserMaster> getRoleByEmail(String email);
}
package com.HighwayManagment.ATMS_WebApp.serviceIMPL.system;

import com.HighwayManagment.ATMS_WebApp.dto.system.FlatMenuPermission;
import com.HighwayManagment.ATMS_WebApp.dto.system.UserMenuTransactionDTO;
import com.HighwayManagment.ATMS_WebApp.entity.enumData.UserRole;
import com.HighwayManagment.ATMS_WebApp.entity.system.UserMenuMaster;
import com.HighwayManagment.ATMS_WebApp.entity.system.UserMenuTransaction;
import com.HighwayManagment.ATMS_WebApp.repository.system.UserMenuMasterRepo;
import com.HighwayManagment.ATMS_WebApp.repository.system.UserMenuTransactionRepo;
import com.HighwayManagment.ATMS_WebApp.service.system.UserMenuTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserMenuTransactionServiceImpl implements UserMenuTransactionService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserMenuTransactionServiceImpl.class);

    @Autowired
    private UserMenuMasterRepo menuRepo;

    @Autowired
    private UserMenuTransactionRepo transactionRepo;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseEntity<String> assignPermission(UserMenuTransactionDTO dto) {

        logger.info("Assign permission started for Role={} MenuId={}",
                dto.getUserRole(), dto.getMenuId());

        UserMenuMaster menu = menuRepo.findById(dto.getMenuId())
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "error.menu.notfound",
                                null,
                                Locale.getDefault()
                        )
                ));

        boolean exists = transactionRepo.existsByUserRoleAndMenu(
                dto.getUserRole(),
                menu
        );

        if (exists) {

            logger.warn("Permission already exists");

            return ResponseEntity.badRequest().body(
                    messageSource.getMessage(
                            "error.permission.exists",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        UserMenuTransaction tx = new UserMenuTransaction();

        tx.setUserRole(dto.getUserRole());
        tx.setMenu(menu);
        tx.setCanAdd(dto.getCanAdd());
        tx.setCanUpdate(dto.getCanUpdate());
        tx.setCanDelete(dto.getCanDelete());
        tx.setCanDisplay(dto.getCanDisplay());
        tx.setIsActive(dto.getIsActive());

        transactionRepo.save(tx);

        logger.info("Permission assigned successfully");

        return ResponseEntity.ok(
                messageSource.getMessage(
                        "success.permission.assigned",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @Override
    public List<UserMenuTransactionDTO> getUserPermissionsByRole(UserRole userRole) {

        logger.info("Fetching permissions for role={}", userRole);

        List<UserMenuTransaction> transactions = transactionRepo.findByUserRoleAndIsActiveTrue(userRole);

        Map<Long, UserMenuTransactionDTO> dtoMap = new HashMap<>();

        for (UserMenuTransaction tx : transactions) {

            UserMenuMaster menu = tx.getMenu();

            UserMenuTransactionDTO dto = new UserMenuTransactionDTO();

            dto.setMenuTrnId(tx.getMenuTrnId());
            dto.setUserRole(tx.getUserRole());
            dto.setMenuId(menu.getMenuId());
            dto.setCanAdd(tx.getCanAdd());
            dto.setCanUpdate(tx.getCanUpdate());
            dto.setCanDelete(tx.getCanDelete());
            dto.setCanDisplay(tx.getCanDisplay());
            dto.setIsActive(tx.getIsActive());
            dto.setMenuName(menu.getMenuName());
            dto.setMenuUrl(menu.getMenuUrl());
            dto.setIcon(menu.getIcon());
            dto.setParentId(menu.getParentId());

            dtoMap.put(menu.getMenuId(), dto);
        }

        List<UserMenuTransactionDTO> rootMenus = new ArrayList<>();

        for (UserMenuTransactionDTO dto : dtoMap.values()) {
            if (dto.getParentId() == null ||
                    !dtoMap.containsKey(dto.getParentId())) {
                rootMenus.add(dto);
            } else {

                dtoMap.get(dto.getParentId())
                        .getChildren()
                        .add(dto);
            }
        }
        return rootMenus;
    }

    @Override
    public List<FlatMenuPermission> getUserPermissionsFlat(UserRole userRole) {

        logger.info("Fetching FLAT permissions for role={}", userRole);

        List<UserMenuTransaction> transactions = transactionRepo.findByUserRoleAndIsActiveTrue(userRole);

        List<FlatMenuPermission> dtoList = new ArrayList<>();

        for (UserMenuTransaction tx : transactions) {

            UserMenuMaster menu = tx.getMenu();

            FlatMenuPermission dto = new FlatMenuPermission();

            dto.setMenuTrnId(tx.getMenuTrnId());
            dto.setUserRole(tx.getUserRole().name()); // if enum
            dto.setMenuId(menu.getMenuId());

            dto.setCanAdd(tx.getCanAdd());
            dto.setCanUpdate(tx.getCanUpdate());
            dto.setCanDelete(tx.getCanDelete());
            dto.setCanDisplay(tx.getCanDisplay());

            dto.setIsActive(tx.getIsActive());

            dto.setMenuName(menu.getMenuName());
            dto.setMenuUrl(menu.getMenuUrl());
            dto.setIcon(menu.getIcon());

            dto.setParentId(menu.getParentId());

            dtoList.add(dto);
        }

        return dtoList;
    }

//    @Override
//    public List<UserMenuTransactionDTO> getUserPermissionsFlat(UserRole userRole) {
//
//        logger.info("Fetching FLAT permissions for role={}", userRole);
//
//        List<UserMenuTransaction> transactions =
//                transactionRepo.findByUserRoleAndIsActiveTrue(userRole);
//
//        List<UserMenuTransactionDTO> dtoList = new ArrayList<>();
//
//        for (UserMenuTransaction tx : transactions) {
//            UserMenuMaster menu = tx.getMenu();
//            UserMenuTransactionDTO dto = new UserMenuTransactionDTO();
//
//            dto.setMenuTrnId(tx.getMenuTrnId());
//            dto.setUserRole(tx.getUserRole());
//            dto.setMenuId(menu.getMenuId());
//            dto.setCanAdd(tx.getCanAdd());
//            dto.setCanUpdate(tx.getCanUpdate());
//            dto.setCanDelete(tx.getCanDelete());
//            dto.setCanDisplay(tx.getCanDisplay());
//            dto.setIsActive(tx.getIsActive());
//            dto.setMenuName(menu.getMenuName());
//            dto.setMenuUrl(menu.getMenuUrl());
//            dto.setIcon(menu.getIcon());
//            dto.setParentId(menu.getParentId());
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }

    @Override
    public ResponseEntity<String> assignPermissionsBulk(
            List<UserMenuTransactionDTO> dtoList) {
        try {
            for (UserMenuTransactionDTO dto : dtoList) {
                boolean hasPermission =
                        Boolean.TRUE.equals(dto.getCanAdd()) ||
                                Boolean.TRUE.equals(dto.getCanUpdate()) ||
                                Boolean.TRUE.equals(dto.getCanDelete()) ||
                                Boolean.TRUE.equals(dto.getCanDisplay());

                UserMenuMaster menu = menuRepo.findById(dto.getMenuId())
                        .orElseThrow(() -> new RuntimeException(
                                messageSource.getMessage(
                                        "error.menu.notfound",
                                        null,
                                        Locale.getDefault()
                                )
                        ));

                Optional<UserMenuTransaction> existingOpt =
                        transactionRepo.findByUserRoleAndMenu(
                                dto.getUserRole(),
                                menu
                        );

                if (!hasPermission) {
                    existingOpt.ifPresent(transactionRepo::delete);
                    continue;
                }

                UserMenuTransaction tx =
                        existingOpt.orElseGet(UserMenuTransaction::new);

                tx.setUserRole(dto.getUserRole());

                tx.setMenu(menu);

                tx.setCanAdd(dto.getCanAdd());
                tx.setCanUpdate(dto.getCanUpdate());
                tx.setCanDelete(dto.getCanDelete());
                tx.setCanDisplay(dto.getCanDisplay());

                tx.setIsActive(dto.getIsActive());

                transactionRepo.save(tx);
            }

            return ResponseEntity.ok(
                    messageSource.getMessage(
                            "success.permission.bulk",
                            null,
                            Locale.getDefault()
                    )
            );

        } catch (Exception e) {

            logger.error("Bulk save failed", e);

            return ResponseEntity.internalServerError()
                    .body("Bulk permission save failed: " + e.getMessage());
        }
    }
    @Override
    public void deleteMenuTrns(Long id) {

        transactionRepo.findById(id)
                .ifPresentOrElse(tx -> {

                    tx.setIsActive(Boolean.FALSE);

                    transactionRepo.save(tx);

                }, () -> {

                    throw new RuntimeException(
                            messageSource.getMessage(
                                    "error.menutrns.notfound",
                                    new Object[]{id},
                                    Locale.getDefault()
                            )
                    );
                });
    }
}

package com.HighwayManagment.ATMS_WebApp.serviceIMPL.system;

import com.HighwayManagment.ATMS_WebApp.dto.system.UserMenuMasterDTO;
import com.HighwayManagment.ATMS_WebApp.entity.system.UserMenuMaster;
import com.HighwayManagment.ATMS_WebApp.repository.system.UserMenuMasterRepo;
import com.HighwayManagment.ATMS_WebApp.security.JwtService;
import com.HighwayManagment.ATMS_WebApp.service.system.UserMenuMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserMenuMasterServiceImpl implements UserMenuMasterService {

    private static final Logger logger = LoggerFactory.getLogger(UserMenuMasterServiceImpl.class);

    @Autowired
    private UserMenuMasterRepo userMenuMasterRepository;

    @Autowired
    private JwtService jwtService;

    // ---------------------- SAVE -----------------------
    @Override
    public UserMenuMaster saveMenu(UserMenuMaster menu) {
        logger.info("Received request to save a new menu.");
        UserMenuMaster savedMenu = userMenuMasterRepository.save(menu);
        logger.info("Menu saved successfully with ID: {}", savedMenu.getMenuId());
        return savedMenu;
    }

    // ---------------------- UPDATE -----------------------
    @Override
    public UserMenuMaster updateMenu(Long id, UserMenuMaster menuDetails) {
        logger.info("Received request to update Menu ID: {}", id);
        UserMenuMaster existingMenu = userMenuMasterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with ID: " + id));

        existingMenu.setMenuName(menuDetails.getMenuName());
        existingMenu.setMenuUrl(menuDetails.getMenuUrl());
        existingMenu.setIcon(menuDetails.getIcon());
        existingMenu.setParentId(menuDetails.getParentId());
        existingMenu.setIsActive(menuDetails.getIsActive());

        UserMenuMaster updated = userMenuMasterRepository.save(existingMenu);
        logger.info("Menu updated successfully for ID: {}", id);
        return updated;
    }

//    @Override
//    public List<UserMenuMasterDTO> getAllMenus() {
//
//        logger.info("Fetching all menus for language={}");
//
//        List<UserMenuMaster> menus = userMenuMasterRepository.findAll();
//
//        return menus.stream().map(menu -> {
//
//            UserMenuMasterDTO dto = new UserMenuMasterDTO();
//
//            // Main Menu (Parent)
//            if (menu.getParentId() == null || menu.getParentId() == 0) {
//                dto.setMainMenu("Root Menu");
//            } else {
//                UserMenuMaster parent = userMenuMasterRepository.findById(menu.getParentId()).orElse(null);
//                dto.setMainMenu( parent != null ? "Main Menu" : "Unknown Parent");
//            }
//            //getMenuNameByLanguage(parent.getMenuId(), languageCode)
//            dto.setMenuName(menu.getMenuName());
//            dto.setUrl(menu.getMenuUrl());
//            dto.setIconUrl("/assets/icons/" + menu.getIcon());
//            dto.setActive(menu.getIsActive() ? "Active" : "Inactive");
//            dto.setMenuId(menu.getMenuId());
//
//            return dto;
//
//        }).collect(Collectors.toList());
//    }
@Override
public List<UserMenuMasterDTO> getAllMenus() {
    logger.info("Fetching all menus in hierarchical tree view format");

    // 1. Fetch all entity records from database (Single Query)
    List<UserMenuMaster> menus = userMenuMasterRepository.findAll();

    // 2. Map Entities to DTOs and store in a Map for quick ID lookup
    Map<Long, UserMenuMasterDTO> dtoMap = menus.stream().map(menu -> {
        UserMenuMasterDTO dto = new UserMenuMasterDTO();

        // Raw properties
        dto.setMenuId(menu.getMenuId());
        dto.setMenuName(menu.getMenuName());
        dto.setMenuUrl(menu.getMenuUrl());
        dto.setIcon(menu.getIcon());
        dto.setParentId(menu.getParentId());
        dto.setIsActive(menu.getIsActive());

        // Derived/Formatted properties
        dto.setSubMenu(menu.getMenuName()); // Default fallback for translated name
        dto.setUrl(menu.getMenuUrl());
        dto.setIconUrl(menu.getIcon());
        dto.setActive(menu.getIsActive() != null && menu.getIsActive() ? "Active" : "Inactive");

        return dto;
    }).collect(Collectors.toMap(UserMenuMasterDTO::getMenuId, dto -> dto));

    // 3. Build the Hierarchy Tree
    List<UserMenuMasterDTO> rootMenus = new ArrayList<>();

    for (UserMenuMasterDTO dto : dtoMap.values()) {
        Long parentId = dto.getParentId();

        // Check if it is a Root Menu
        if (parentId == null || parentId == 0) {
            dto.setMainMenu("Root Menu");
            rootMenus.add(dto);
        } else {
            // Find parent DTO in memory map
            UserMenuMasterDTO parentDto = dtoMap.get(parentId);
            if (parentDto != null) {
                dto.setMainMenu(parentDto.getMenuName()); // Sets actual parent name
                parentDto.getChildren().add(dto);        // Nest child into parent
            } else {
                dto.setMainMenu("Unknown Parent");
                rootMenus.add(dto); // Orphan menus are treated as roots to avoid losing data
            }
        }
    }

    return rootMenus; // Returns only the top-level parent menus containing nested children
}
    // ---------------------- DELETE STATUS -----------------------
    @Override
    public void DeleteStatus(Long id) {
        logger.info("Received request to deactivate Menu ID: {}", id);

        userMenuMasterRepository.findById(id).ifPresentOrElse(menu -> {
            menu.setIsActive(Boolean.FALSE);
            userMenuMasterRepository.save(menu);

            logger.info("Menu ID: {} successfully deactivated.", id);

        }, () -> {
            logger.error("Menu not found with ID: {}", id);
            throw new RuntimeException("Menu not found with ID: " + id);
        });
    }
}

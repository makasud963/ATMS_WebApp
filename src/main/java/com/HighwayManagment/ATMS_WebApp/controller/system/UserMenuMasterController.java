package com.HighwayManagment.ATMS_WebApp.controller.system;
import com.HighwayManagment.ATMS_WebApp.dto.system.UserMenuMasterDTO;
import com.HighwayManagment.ATMS_WebApp.entity.system.UserMenuMaster;
import com.HighwayManagment.ATMS_WebApp.service.system.UserMenuMasterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
public class UserMenuMasterController {

    private static final Logger logger = LoggerFactory.getLogger(UserMenuMasterController.class);

    @Autowired
    private UserMenuMasterService userMenuMasterService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/save")
    public ResponseEntity<String> saveMenu(
            @Valid @RequestBody UserMenuMasterDTO dto,
            @RequestHeader("Authorization") String token) {

        logger.info("Saving new menu: {}", dto);

        UserMenuMaster menu = mapDtoToEntity(dto);

        UserMenuMaster savedMenu = userMenuMasterService.saveMenu(menu);
        logger.info("English record saved with ID: {}", savedMenu.getMenuId());

        String msg = messageSource.getMessage("menu.created.success", null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMenu(
            @PathVariable Long id,
            @Valid @RequestBody UserMenuMasterDTO dto,
            @RequestHeader("Authorization") String token) {

        logger.info("Updating menu ID: {} with data: {}", id, dto);
        UserMenuMaster updatedMenu = userMenuMasterService.updateMenu(id, mapDtoToEntity(dto));
        logger.info("English fields updated for menu ID: {}", id);

        String msg = messageSource.getMessage("menu.updated.success",
                new Object[]{id}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id) {

        logger.info("Deleting menu ID: {}", id);
        userMenuMasterService.DeleteStatus(id);

        String msg = messageSource.getMessage("menu.deleted.success",
                new Object[]{id}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(msg);
    }

    @GetMapping()
    public ResponseEntity<?> getAllMenus() {
        //@PathVariable String languageCode
        logger.info("Fetching menus for Language Code:"); //languageCode

        var menus = userMenuMasterService.getAllMenus();

        logger.info("Returning {} menus", menus.size());
        return ResponseEntity.ok(menus);
    }

    private UserMenuMaster mapDtoToEntity(UserMenuMasterDTO dto) {
        logger.info("Mapping UserMenuMasterDTO to Entity.");

        UserMenuMaster menu = new UserMenuMaster();
        menu.setMenuName(dto.getMenuName()); // English name
        menu.setMenuUrl(dto.getMenuUrl());
        menu.setIcon(dto.getIcon());
        menu.setParentId(dto.getParentId());
        menu.setIsActive(dto.getIsActive());
        return menu;
    }

    private String cleanToken(String token) {
        logger.info("Cleaning Authorization token.");
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}

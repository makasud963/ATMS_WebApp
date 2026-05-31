package com.HighwayManagment.ATMS_WebApp.controller.system;
import com.HighwayManagment.ATMS_WebApp.dto.system.FlatMenuPermission;
import com.HighwayManagment.ATMS_WebApp.dto.system.UserMenuTransactionDTO;
import com.HighwayManagment.ATMS_WebApp.entity.UserMaster;
import com.HighwayManagment.ATMS_WebApp.entity.enumData.UserRole;
import com.HighwayManagment.ATMS_WebApp.repository.UserRepository;
import com.HighwayManagment.ATMS_WebApp.security.JwtService;
import com.HighwayManagment.ATMS_WebApp.service.system.UserMenuTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menutrn")
public class UserMenuTransactionController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserMenuTransactionController.class);

    @Autowired
    private UserMenuTransactionService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/assign")
    public ResponseEntity<String> assignPermissions(
            @RequestBody UserMenuTransactionDTO dto) {
        return service.assignPermission(dto);
    }

    @GetMapping("/role")
    public ResponseEntity<List<UserMenuTransactionDTO>>
    getPermissionsByRole(@RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Received request to get menu by role -" + authorizationHeader);
        String token = authorizationHeader.replace("Bearer ", "").trim();
        logger.info("Recived token=" + token);
        String userEmail =jwtService.extractEmail(token);
        logger.info("Recived Email from user token=" + userEmail);

        UserMaster user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String userRole = user.getRole();

        return ResponseEntity.ok( service.getUserPermissionsByRole(UserRole.valueOf(userRole))
        );
    }
//    @GetMapping("/flat/{userRole}")
//    public ResponseEntity<List<UserMenuTransactionDTO>>
//    getFlatPermissions(@PathVariable UserRole userRole) {
//        return ResponseEntity.ok(
//                service.getUserPermissionsFlat(userRole)
//        );
//    }

    @GetMapping("/flat/{userRole}")
    public ResponseEntity<List<FlatMenuPermission>>
    getFlatPermissions(@PathVariable UserRole userRole) {
        return ResponseEntity.ok(
                service.getUserPermissionsFlat(userRole)
        );
    }


    @PostMapping("/bulk")
    public ResponseEntity<String> assignPermissionsBulk(
            @RequestBody List<UserMenuTransactionDTO> dtoList) {
        logger.info("Received bulk permission request");
        return service.assignPermissionsBulk(dtoList);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteMenuTrns(id);
        return ResponseEntity.ok("Permission deleted successfully");
    }
}
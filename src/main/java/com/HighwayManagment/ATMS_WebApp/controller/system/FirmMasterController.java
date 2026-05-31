package com.HighwayManagment.ATMS_WebApp.controller.system;

import com.HighwayManagment.ATMS_WebApp.dto.system.FirmMasterDTO;
import com.HighwayManagment.ATMS_WebApp.service.system.FirmMasterService;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auth/firms")
public class FirmMasterController {

    private static final Logger logger = LoggerFactory.getLogger(FirmMasterController.class);

    @Autowired
    private FirmMasterService firmService;

    @Autowired
    private MessageSource messageSource;

    @SneakyThrows
    @PostMapping("/save")
    public FirmMasterDTO createFirm(@Valid @RequestBody FirmMasterDTO dto) {
        logger.info("Received request to create firm: {}", dto);
        FirmMasterDTO createdFirm = firmService.createFirm(dto);
        String msg = messageSource.getMessage("firm.created.success", new Object[]{createdFirm.getFirmId()}, LocaleContextHolder.getLocale());
        logger.info("Firm created successfully. Message: {}", msg);
        return createdFirm;
    }

    @GetMapping
    public List<FirmMasterDTO> getAllFirms() {
        logger.info("Received request to fetch all firms.");
        List<FirmMasterDTO> firms = firmService.getAllFirms();
        logger.info("Fetched {} firms.", firms.size());
        return firms;
    }

    @GetMapping("/{id}")
    public FirmMasterDTO getFirmById(@PathVariable Integer id) {
        logger.info("Received request to fetch firm with ID: {}", id);
        FirmMasterDTO firm = firmService.getFirmById(id);
        logger.info("Fetched firm details for ID: {}", id);
        return firm;
    }

    @PutMapping("/update/{id}")
    public FirmMasterDTO updateFirm(@PathVariable Integer id, @Valid @RequestBody FirmMasterDTO dto) {
        logger.info("Received request to update firm with ID: {} and data: {}", id, dto);
        FirmMasterDTO updatedFirm = firmService.updateFirm(id, dto);
        String msg = messageSource.getMessage("firm.updated.success", new Object[]{updatedFirm.getFirmId()}, LocaleContextHolder.getLocale());
        logger.info("Firm updated successfully. Message: {}", msg);
        return updatedFirm;
    }

    @DeleteMapping("/delete/{id}")
    public void softDeleteFirm(@PathVariable Integer id, @RequestParam String deletedBy) {
        logger.info("Received request to soft delete firm with ID: {} by user: {}", id, deletedBy);
        firmService.softDeleteFirm(id, deletedBy);
        String msg = messageSource.getMessage("firm.deleted.success", new Object[]{id}, LocaleContextHolder.getLocale());
        logger.info("Firm soft deleted successfully. Message: {}", msg);
    }
}

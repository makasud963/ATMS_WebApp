package com.HighwayManagment.ATMS_WebApp.serviceIMPL.system;
import com.HighwayManagment.ATMS_WebApp.dto.system.SideMasterDTO;
import com.HighwayManagment.ATMS_WebApp.entity.system.FirmMaster;
import com.HighwayManagment.ATMS_WebApp.entity.system.SideMaster;
import com.HighwayManagment.ATMS_WebApp.repository.system.FirmMasterRepository;
import com.HighwayManagment.ATMS_WebApp.repository.system.SideMasterRepo;
import com.HighwayManagment.ATMS_WebApp.service.system.SideMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class SideMasterServiceImpl implements SideMasterService {

    private static final Logger logger =
            LoggerFactory.getLogger(SideMasterServiceImpl.class);

    @Autowired
    private SideMasterRepo sideRepo;

    @Autowired
    private FirmMasterRepository firmRepo;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseEntity<String> createSide(SideMasterDTO dto) {

        logger.info("Create side process started for SideName={}", dto.getSideName());

        boolean exists = sideRepo.existsBySideName(dto.getSideName());

        if (exists) {

            logger.warn("Side with name {} already exists", dto.getSideName());

            return ResponseEntity.badRequest().body(
                    messageSource.getMessage(
                            "error.side.exists",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        FirmMaster firm = firmRepo.findById(dto.getFirmId())
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "error.firm.notfound",
                                null,
                                Locale.getDefault()
                        )
                ));

        SideMaster side = new SideMaster();

        side.setSideName(dto.getSideName());
        side.setSidePhoneNumber(dto.getSidePhoneNumber());
        side.setSideEmailIid(dto.getSideEmailId());
        side.setFirmId(firm);
        side.setSideDeleteStatus(false);

        sideRepo.save(side);

        logger.info("Side created successfully");

        return ResponseEntity.ok(
                messageSource.getMessage(
                        "success.side.created",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @Override
    public List<SideMasterDTO> getAllActiveSides() {

        logger.info("Fetching all active sides");

        List<SideMaster> sides = sideRepo.findBySideDeleteStatusFalse();

        List<SideMasterDTO> dtoList = new ArrayList<>();

        for (SideMaster side : sides) {

            SideMasterDTO dto = new SideMasterDTO();

            dto.setSideId(side.getSideId());
            dto.setSideName(side.getSideName());
            dto.setSidePhoneNumber(side.getSidePhoneNumber());
            dto.setSideEmailId(side.getSideEmailIid());
            dto.setFirmId(side.getFirmId().getFirmId());
            dto.setSideDeleteStatus(side.isSideDeleteStatus());

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public SideMasterDTO getSideById(Integer sideId) {

        logger.info("Fetching side details for Id={}", sideId);

        SideMaster side = sideRepo.findBySideIdAndSideDeleteStatusFalse(sideId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "error.side.notfound",
                                null,
                                Locale.getDefault()
                        )
                ));

        SideMasterDTO dto = new SideMasterDTO();

        dto.setSideId(side.getSideId());
        dto.setSideName(side.getSideName());
        dto.setSidePhoneNumber(side.getSidePhoneNumber());
        dto.setSideEmailId(side.getSideEmailIid());
        dto.setFirmId(side.getFirmId().getFirmId());
        dto.setSideDeleteStatus(side.isSideDeleteStatus());

        return dto;
    }

    @Override
    public ResponseEntity<String> updateSide(Integer sideId, SideMasterDTO dto) {

        logger.info("Updating side process started for Id={}", sideId);

        SideMaster side = sideRepo.findBySideIdAndSideDeleteStatusFalse(sideId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "error.side.notfound",
                                null,
                                Locale.getDefault()
                        )
                ));

        // Check if the modified side name conflicts with another active side record
        if (!side.getSideName().equalsIgnoreCase(dto.getSideName())) {

            boolean exists = sideRepo.existsBySideName(dto.getSideName());

            if (exists) {

                logger.warn("Side name update conflict: {} already exists", dto.getSideName());

                return ResponseEntity.badRequest().body(
                        messageSource.getMessage(
                                "error.side.exists",
                                null,
                                Locale.getDefault()
                        )
                );
            }
        }

        FirmMaster firm = firmRepo.findById(dto.getFirmId())
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "error.firm.notfound",
                                null,
                                Locale.getDefault()
                        )
                ));

        side.setSideName(dto.getSideName());
        side.setSidePhoneNumber(dto.getSidePhoneNumber());
        side.setSideEmailIid(dto.getSideEmailId());
        side.setFirmId(firm);

        sideRepo.save(side);

        logger.info("Side updated successfully");

        return ResponseEntity.ok(
                messageSource.getMessage(
                        "success.side.updated",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @Override
    public ResponseEntity<String> deleteSide(Integer sideId) {

        logger.info("Soft deleting side with Id={}", sideId);

        SideMaster side = sideRepo.findById(sideId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "error.side.notfound",
                                null,
                                Locale.getDefault()
                        )
                ));

        side.setSideDeleteStatus(true);

        sideRepo.save(side);

        logger.info("Side soft-deleted successfully");

        return ResponseEntity.ok(
                messageSource.getMessage(
                        "success.side.deleted",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
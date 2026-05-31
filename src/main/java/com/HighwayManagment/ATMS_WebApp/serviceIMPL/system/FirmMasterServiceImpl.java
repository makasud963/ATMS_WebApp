package com.HighwayManagment.ATMS_WebApp.serviceIMPL.system;
import com.HighwayManagment.ATMS_WebApp.dto.system.FirmMasterDTO;
import com.HighwayManagment.ATMS_WebApp.entity.system.FirmMaster;
import com.HighwayManagment.ATMS_WebApp.repository.system.FirmMasterRepository;
import com.HighwayManagment.ATMS_WebApp.service.system.FirmMasterService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FirmMasterServiceImpl implements FirmMasterService {

    @Autowired
    private FirmMasterRepository firmRepo;

    private static final Logger logger = LoggerFactory.getLogger(FirmMasterServiceImpl.class);

    private Locale locale() {
        return LocaleContextHolder.getLocale();
    }

    private FirmMasterDTO convertToDto(FirmMaster entity) {
        FirmMasterDTO dto = new FirmMasterDTO();
        org.springframework.beans.BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    private FirmMaster convertToEntity(FirmMasterDTO dto) {
        FirmMaster entity = new FirmMaster();
        org.springframework.beans.BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    @Override
    @Transactional
    public FirmMasterDTO createFirm(FirmMasterDTO dto) {
        logger.info("save Action performed in FirmMasterDto.");
        FirmMaster firm = convertToEntity(dto);
        FirmMaster saved = firmRepo.save(firm);

        return convertToDto(saved);
    }

    @Override
    public List<FirmMasterDTO> getAllFirms() {
        logger.info("Performed find By Firm Delete Status False.");
        return firmRepo.findByFirmDeleteStatusFalse()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FirmMasterDTO getFirmById(Integer id) {
        logger.info("Performed find By Firm By Id.");
        Optional<FirmMaster> firm = firmRepo.findById(id);
        return firm.map(this::convertToDto).orElse(null);
    }

    @Override
    @Transactional
    public FirmMasterDTO updateFirm(Integer id, FirmMasterDTO dto) {
        Optional<FirmMaster> optionalFirm = firmRepo.findById(id);
        logger.info("Performed update Firm.");

        if (optionalFirm.isPresent()) {
            FirmMaster firm = optionalFirm.get();

            org.springframework.beans.BeanUtils.copyProperties(dto, firm, "firmId", "firmEnteredDate");
            FirmMaster updated = firmRepo.save(firm);
            return convertToDto(updated);
        }
        return null;
    }

    @Override
    @Transactional
    public void softDeleteFirm(Integer id, String deletedBy) {
        Optional<FirmMaster> firmOpt = firmRepo.findById(id);
        logger.info("Performed soft Delete Firm.");
        firmOpt.ifPresent(firm -> {
            firm.setFirmDeleteStatus(true);
            firmRepo.save(firm);
        });
    }
}
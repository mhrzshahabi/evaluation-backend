package com.nicico.evaluation.service;

import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.WorkSpaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class WorkSpaceService implements IWorkSpaceService {

    private final WorkSpaceMapper mapper;
    private final ICatalogService catalogService;
    private final IMeritComponentService meritComponentService;
    private final IMeritComponentAuditService meritComponentAuditService;

    @Override
    @Transactional(readOnly = true)
    public List<WorkSpaceDTO.Info> workSpaceList() {
        List<CatalogDTO.Info> workSpaceCategoryList = catalogService.catalogByCatalogTypeCode("WorkSpaceCategory");
        return mapper.catalogDtoInfoToWorkSpaceDtoInfoList(workSpaceCategoryList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> workSpaceDetail(String code) {
        List<Long> meritComponentIds;
        switch (code) {
            case "workSpace-meritComponent-admin" -> meritComponentIds = meritComponentService.getAdminWorkInWorkSpace();
            case "workSpace-meritComponent-expert" -> meritComponentIds = meritComponentAuditService.getExpertWorkInWorkSpace();
            default -> meritComponentIds = null;
        }
        return meritComponentIds;
    }

}

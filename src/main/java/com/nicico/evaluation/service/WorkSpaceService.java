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

    @Override
    @Transactional(readOnly = true)
    public List<WorkSpaceDTO.Info> workSpaceList() {
        List<CatalogDTO.Info> workSpaceCategoryList = catalogService.catalogByCatalogTypeCode("WorkSpaceCategory");
        return mapper.catalogDtoInfoToWorkSpaceDtoInfoList(workSpaceCategoryList);
    }
}

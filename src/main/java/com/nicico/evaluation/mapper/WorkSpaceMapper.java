package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.WorkSpaceDTO;
import com.nicico.evaluation.iservice.IMeritComponentAuditService;
import com.nicico.evaluation.iservice.IMeritComponentService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class WorkSpaceMapper {

    @Lazy
    @Autowired
    private IMeritComponentService meritComponentService;

    @Lazy
    @Autowired
    private IMeritComponentAuditService meritComponentAuditService;

    @Mappings({
            @Mapping(target = "number", source = "code", qualifiedByName = "getNumberOfWorkByCatalogCode")
    })
    public abstract WorkSpaceDTO.Info catalogDtoInfoToWorkSpaceDtoInfo(CatalogDTO.Info catalogDTO);

    public abstract List<WorkSpaceDTO.Info> catalogDtoInfoToWorkSpaceDtoInfoList(List<CatalogDTO.Info> catalogDTOList);

    @Named("getNumberOfWorkByCatalogCode")
    Integer getNumberOfWorkByCatalogCode(String code) {
        Integer numberOfWork;
        switch (code) {
            case "workSpace-meritComponent-admin" -> numberOfWork = meritComponentService.getNumberOfAdminWorkInWorkSpace();
            case "workSpace-meritComponent-expert" -> numberOfWork = meritComponentAuditService.getNumberOfExpertWorkInWorkSpace();
            default -> numberOfWork = 0;
        }
        return numberOfWork;
    }

}

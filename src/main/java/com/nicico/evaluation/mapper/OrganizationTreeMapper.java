package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.model.OrganizationTree;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationTreeMapper {
    OrganizationTreeDTO.Info entityToDtoInfo(OrganizationTree entity);

    List<OrganizationTreeDTO.Info> entityToDtoInfoList(List<OrganizationTree> entities);

    List<OrganizationTreeDTO.InfoTree> entityToDtoInfoTreeList(List<OrganizationTree> entities);

}

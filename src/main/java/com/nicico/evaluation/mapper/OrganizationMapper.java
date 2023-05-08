package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.OrganizationDTO;
import com.nicico.evaluation.model.Organization;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    OrganizationDTO.Info entityToDtoInfo(Organization entity);

    List<OrganizationDTO.Info> entityToDtoInfoList(List<Organization> entities);

}

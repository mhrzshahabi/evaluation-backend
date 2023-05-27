package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.model.OrganizationTree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrganizationTreeMapper {

    @Lazy
    @Autowired
    private IOrganizationTreeService organizationTreeService;

    public abstract OrganizationTreeDTO.Info entityToDtoInfo(OrganizationTree entity);

    public abstract OrganizationTreeDTO.InfoDetail entityToDtoInfoDetail(OrganizationTree entity);

    public abstract List<OrganizationTreeDTO.Info> entityToDtoInfoList(List<OrganizationTree> entities);

    @Mappings({
            @Mapping(target = "nameFa", source = "entity", qualifiedByName = "getNameFaFromEntity"),
            @Mapping(target = "childNode", source = "postId", qualifiedByName = "getChildNodeFromPostId")
    })
    public abstract OrganizationTreeDTO.InfoTree entityToDtoInfoTree(OrganizationTree entity);

    public abstract List<OrganizationTreeDTO.InfoTree> entityToDtoInfoTreeList(List<OrganizationTree> entities);

    @Named("getNameFaFromEntity")
    String getNameFaFromEntity(OrganizationTree entity) {
        return String.format("%s|%s|%s|%s",
                entity.getNationalCode(),
                entity.getPostCode(),
                entity.getFullName(),
                entity.getPostTitle()
        );
    }

    @Named("getChildNodeFromPostId")
    Long getChildNodeFromPostId(Long postId) {
        return organizationTreeService.countChildNode(postId);
    }

}

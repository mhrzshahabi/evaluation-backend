package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.model.OrganizationTree;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrganizationTreeMapper {

    @Lazy
    @Autowired
    private IOrganizationTreeService organizationTreeService;

    public abstract OrganizationTreeDTO.Info entityToDtoInfo(OrganizationTree entity);

    public abstract OrganizationTreeDTO.InfoDetail entityToDtoInfoDetail(OrganizationTree entity);

    public abstract List<OrganizationTreeDTO.Info> entityToDtoInfoList(List<OrganizationTree> entities);

    public List<OrganizationTreeDTO.InfoTree> entityToDtoInfoTreeList(List<OrganizationTree> entities) {
        List<OrganizationTreeDTO.InfoTree> infoTrees = new ArrayList<>();

        for (OrganizationTree org : entities) {
            OrganizationTreeDTO.InfoTree infoTree = new OrganizationTreeDTO.InfoTree();
            infoTree.setId((org.getId()));
            infoTree.setPostParentId((org.getPostParentId()));

            infoTree.setNameFa(String.format("%s|%s|%s|%s",
                    org.getNationalCode(),
                    org.getPostCode(),
                    org.getFullName(),
                    org.getPostTitle()
            ));
            infoTree.setChildNode(organizationTreeService.countChildNode(org.getPostId()));
            infoTrees.add(infoTree);
        }
        return infoTrees;
    }

}
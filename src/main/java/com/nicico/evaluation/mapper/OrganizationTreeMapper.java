package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.model.OrganizationTree;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationTreeMapper {
    OrganizationTreeDTO.Info entityToDtoInfo(OrganizationTree entity);

    List<OrganizationTreeDTO.Info> entityToDtoInfoList(List<OrganizationTree> entities);

    default  List<OrganizationTreeDTO.InfoTree> entityToDtoInfoTreeList(List<OrganizationTree> entities){
        List<OrganizationTreeDTO.InfoTree> infoTrees = new ArrayList<>();

        for (OrganizationTree org: entities) {
            OrganizationTreeDTO.InfoTree infoTree = new OrganizationTreeDTO.InfoTree();
            infoTree.setId((org.getId()));
            infoTree.setPostParentId((org.getPostParentId()));

            infoTree.setNameFa(String.format("%s|%s|%s|%s" ,
                    org.getNationalCode() ,
                    org.getPostCode(),
                    org.getFullName(),
                    org.getPostTitle()
                    ));
            infoTrees.add(infoTree);
        }
        return  infoTrees;
    }

}

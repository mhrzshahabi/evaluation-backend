package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.OrganizationTreeDTO;

import java.util.List;

public interface IOrganizationTreeService {
    List<OrganizationTreeDTO.Info> get(Long orgStructureId);

    OrganizationTreeDTO.SpecResponse list(int count, int startIndex);

    List<OrganizationTreeDTO.InfoTree> listTree(int count, int startIndex, Long parentId);

    Long countChildNode(Long parentId);

    SearchDTO.SearchRs<OrganizationTreeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;
}

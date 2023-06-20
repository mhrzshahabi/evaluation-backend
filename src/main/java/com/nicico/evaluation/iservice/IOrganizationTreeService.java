package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.OrganizationTreeDTO;

import java.util.List;

public interface IOrganizationTreeService {
    List<OrganizationTreeDTO.InfoTree> get(Long orgStructureId);

    OrganizationTreeDTO.InfoDetail getDetail(Long id);

    List<OrganizationTreeDTO.InfoTree> list(int count, int startIndex, Long orgStructureId, Long postParentId);

    Long countChildNode(Long postId);

    SearchDTO.SearchRs<OrganizationTreeDTO.InfoTree> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    OrganizationTreeDTO.InfoTree getByPostCode(String postCode);

}

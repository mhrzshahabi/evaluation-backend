package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.OrganizationTreeDTO;

import java.util.List;

public interface IOrganizationTreeService {
    List<OrganizationTreeDTO.InfoTree> get(Long orgStructureId);

    List<OrganizationTreeDTO.InfoTree> getByPostIds(List<Long> postIds);

    List<String> getByParentNationalCode();

    OrganizationTreeDTO.InfoDetail getDetail(Long id);

    List<OrganizationTreeDTO.InfoTree> list(int count, int startIndex, Long orgStructureId, Long postParentId);

    Long countChildNode(Long postId);

    List<OrganizationTreeDTO.InfoTree> listTree(int count, int startIndex, Long orgStructureId, Long postParentId);

    SearchDTO.SearchRs<OrganizationTreeDTO.InfoTree> search(SearchDTO.SearchRq request, Long orgStructureId) throws IllegalAccessException, NoSuchFieldException;

    OrganizationTreeDTO.InfoTree getByPostCode(String postCode);

    OrganizationTreeDTO.InfoTree getByNationalCode(String nationalCode);

}

package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.model.OrganizationTree;

import java.util.List;

public interface IOrganizationTreeService {
    List<OrganizationTreeDTO.Info> get(Long orgStructureId);

    OrganizationTreeDTO.InfoDetail getDetail(Long id);

    OrganizationTreeDTO.SpecResponse list(int count, int startIndex);

    List<OrganizationTreeDTO.InfoTree> listTree(int count, int startIndex, Long orgStructureId, Long parentId);

    Long countChildNode(Long postId);

    SearchDTO.SearchRs<OrganizationTreeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    OrganizationTreeDTO.Info getByPostCodeAndNationalCode(String postCode, String nationalCode);
    OrganizationTreeDTO.Info getByPostCode(String postCode);

}

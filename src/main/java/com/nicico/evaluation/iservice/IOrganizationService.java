package com.nicico.evaluation.iservice;

import com.nicico.evaluation.dto.OrganizationDTO;

import java.util.List;


public interface IOrganizationService {
    List<OrganizationDTO.Info> get(Long id);

    OrganizationDTO.SpecResponse list(int count, int startIndex);
}

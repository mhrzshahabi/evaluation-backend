package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.OrganizationDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IOrganizationService;
import com.nicico.evaluation.mapper.OrganizationMapper;
import com.nicico.evaluation.model.Organization;
import com.nicico.evaluation.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizationService implements IOrganizationService {
    private final OrganizationRepository repository;
    private final OrganizationMapper mapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION')")
    public OrganizationDTO.Info get(Long id) {
        Organization organization = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(organization);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION')")
    public OrganizationDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Organization> organizations = repository.findAll(pageable);
        List<OrganizationDTO.Info> personInfos = mapper.entityToDtoInfoList(organizations.getContent());

        OrganizationDTO.Response response = new OrganizationDTO.Response();
        OrganizationDTO.SpecResponse specResponse = new OrganizationDTO.SpecResponse();

        if (personInfos != null) {
            response.setData(personInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) organizations.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION')")
    public SearchDTO.SearchRs<OrganizationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

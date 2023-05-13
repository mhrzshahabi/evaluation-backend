package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.mapper.OrganizationTreeMapper;
import com.nicico.evaluation.model.OrganizationTree;
import com.nicico.evaluation.repository.OrganizationTreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizationTreeService implements IOrganizationTreeService {

    private final OrganizationTreeRepository repository;
    private final OrganizationTreeMapper mapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION_TREE')")
    public List<OrganizationTreeDTO.Info> get(Long orgStructureId) {
        List<OrganizationTree> organizationTrees = repository.findAllByOrgStructureId(orgStructureId);
        if (organizationTrees == null || organizationTrees.isEmpty())
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound);
        return mapper.entityToDtoInfoList(organizationTrees);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION_TREE')")
    public List<OrganizationTreeDTO.InfoTree> listTree(int count, int startIndex, Long parentId) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        List<OrganizationTree> organizationTrees = null;
        if (parentId == 0) {
            organizationTrees = repository.findAllByPostParentId(null, pageable);
        } else {
            organizationTrees = repository.findAllByPostParentId(parentId, pageable);
        }
        return mapper.entityToDtoInfoTreeList(organizationTrees);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION_TREE')")
    public OrganizationTreeDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<OrganizationTree> organizationTrees = repository.findAll(pageable);
        List<OrganizationTreeDTO.Info> personInfos = mapper.entityToDtoInfoList(organizationTrees.getContent());

        OrganizationTreeDTO.Response response = new OrganizationTreeDTO.Response();
        OrganizationTreeDTO.SpecResponse specResponse = new OrganizationTreeDTO.SpecResponse();

        if (personInfos != null) {
            response.setData(personInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) organizationTrees.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION_TREE')")
    public SearchDTO.SearchRs<OrganizationTreeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }
}

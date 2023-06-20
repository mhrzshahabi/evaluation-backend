package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.mapper.OrganizationTreeMapper;
import com.nicico.evaluation.model.OrganizationTree;
import com.nicico.evaluation.repository.OrganizationTreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrganizationTreeService implements IOrganizationTreeService {

    private final OrganizationTreeRepository repository;
    private final OrganizationTreeMapper mapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION_TREE')")
    public List<OrganizationTreeDTO.InfoTree> get(Long orgStructureId) {
        List<OrganizationTree> organizationTrees = repository.findAllByOrgStructureId(orgStructureId);
        if (organizationTrees == null || organizationTrees.isEmpty())
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound);
        return mapper.entityToDtoInfoList(organizationTrees);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION_TREE')")
    public OrganizationTreeDTO.InfoDetail getDetail(Long id) {
        OrganizationTree organizationTree = repository.findById(id)
                .orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfoDetail(organizationTree);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION_TREE')")
    public List<OrganizationTreeDTO.InfoTree> list(int count, int startIndex, Long orgStructureId, Long postParentId) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        List<OrganizationTree> organizationTrees;
        if (postParentId == 0) {
            organizationTrees = repository.findAllByPostParentIdAndOrgStructureId(null, orgStructureId, pageable);
        } else {
            organizationTrees = repository.findAllByPostParentIdAndOrgStructureId(postParentId, orgStructureId, pageable);
        }
        return mapper.entityToDtoInfoTreeList(organizationTrees);
    }

    @Override
    public Long countChildNode(Long postId) {
        if (postId > 0)
            return repository.countByPostParentId(postId);
        return 0L;
    }


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ORGANIZATION_TREE')")
    public SearchDTO.SearchRs<OrganizationTreeDTO.InfoTree> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {

        Optional<SearchDTO.CriteriaRq> criteriaNameFa = request.getCriteria().getCriteria().stream()
                .filter(q -> q.getFieldName().equals("nameFa")).findFirst();

        if (criteriaNameFa.isPresent()) {

            String value = criteriaNameFa.get().getValue().get(0).toString();

            final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
            final SearchDTO.CriteriaRq nationalCodeCriteriaRq = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.contains)
                    .setFieldName("nationalCode")
                    .setValue(value);

            final SearchDTO.CriteriaRq postCodeCriteriaRq = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.contains)
                    .setFieldName("postCode")
                    .setValue(value);

            final SearchDTO.CriteriaRq fullNameCriteriaRq = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.contains)
                    .setFieldName("fullName")
                    .setValue(value);

            final SearchDTO.CriteriaRq postTitleCriteriaRq = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.contains)
                    .setFieldName("postTitle")
                    .setValue(value);

            criteriaRqList.add(nationalCodeCriteriaRq);
            criteriaRqList.add(postCodeCriteriaRq);
            criteriaRqList.add(fullNameCriteriaRq);
            criteriaRqList.add(postTitleCriteriaRq);

            final SearchDTO.CriteriaRq criteriaRqList1 = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.or)
                    .setCriteria(criteriaRqList);

            request.setCriteria(criteriaRqList1);
        }
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    public OrganizationTreeDTO.InfoTree getByPostCode(String postCode) {
        OrganizationTree organizationTree = repository.findByPostCode(postCode)
                .orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(organizationTree);
    }
}

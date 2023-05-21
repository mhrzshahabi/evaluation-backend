package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.EvaluationMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.model.Evaluation;
import com.nicico.evaluation.model.OrganizationTree;
import com.nicico.evaluation.model.SpecialCase;
import com.nicico.evaluation.repository.EvaluationRepository;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EvaluationService implements IEvaluationService {

    private final EvaluationMapper mapper;
    private final EvaluationRepository repository;
    private final PageableMapper pageableMapper;
    private final ISpecialCaseService specialCaseService;
    private final IOrganizationTreeService organizationTreeService;
    private final ICatalogService catalogService;

    @Override
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست آیتم های ارزیابی ها");
        return new ExcelGenerator.ExcelDownload(body);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Evaluation> Evaluations = repository.findAll(pageable);
        List<EvaluationDTO.Info> EvaluationInfos = mapper.entityToDtoInfoList(Evaluations.getContent());

        EvaluationDTO.Response response = new EvaluationDTO.Response();
        EvaluationDTO.SpecResponse specResponse = new EvaluationDTO.SpecResponse();

        if (EvaluationInfos != null) {
            response.setData(EvaluationInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) Evaluations.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.Info get(Long id) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(Evaluation);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION')")
    public EvaluationDTO.Info create(EvaluationDTO.Create dto) {
        Evaluation evaluation = mapper.dtoCreateToEntity(dto);
        evaluation = repository.save(evaluation);
        return mapper.entityToDtoInfo(evaluation);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION')")
    public List<EvaluationDTO.Info> createList(List<EvaluationDTO.Create> dto) {
        List<EvaluationDTO.Info> evaluationInfo = new ArrayList<>();
        List<Evaluation> evaluations = mapper.dtoCreateToEntityList(dto);
        for (Evaluation e : evaluations) {
            List<Evaluation> evaluationss =
                    repository.findByEvaluationPeriodIdAndAssessPostCodeAndEndDate(e.getEvaluationPeriodId(), e.getAssessPostCode(), e.getEndDate());
            if (evaluationss.size() > 0) {
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
            }
            SpecialCaseDTO.Info scInfo = specialCaseService.getByAssessNationalCodeAndAssessPostCode(e.getAssessNationalCode(), e.getAssessPostCode());
            if (scInfo != null) {
                e.setAssessorPostCode(scInfo.getAssessorPostCode());
                e.setAssessorNationalCode(scInfo.getAssessorNationalCode());
            } else {
                OrganizationTreeDTO.Info organizationTreeInfo = organizationTreeService.getByPostCodeAndNationalCode(e.getAssessPostCode(), e.getAssessNationalCode());
                e.setAssessorPostCode(organizationTreeInfo.getPostParentCode());
                e.setAssessorNationalCode(organizationTreeInfo.getNationalCodeParent());
            }
            e = repository.save(e);
            evaluationInfo.add(mapper.entityToDtoInfo(e));
        }
        return evaluationInfo;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION')")
    public EvaluationDTO.Info updateStatus(Long id) {
        Evaluation evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        EvaluationDTO.Info evaluationInfo = mapper.entityToDtoInfo(evaluation);
        if (evaluationInfo.getEndDate().compareTo(new Date()) < 0) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
        Long catalogId = null;
        if (evaluation.getStatusCatalog().getCode().equals("Finalized")) {
            catalogId = catalogService.getByCode("Awaiting-review").getId();
        } else {
            catalogId = catalogService.getByCode("Finalized").getId();
        }
        evaluation.setStatusCatalogId(catalogId);
        Evaluation save = repository.save(evaluation);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION')")
    public List<EvaluationDTO.Info> updateStatusAll(List<Long> ids, Long statusCatalogId){
        List<Evaluation> evaluations = repository.findAllByIdIn(ids);
        Long firstStatusCatalogId = evaluations.get(0).getStatusCatalogId();
        if(!evaluations.stream().allMatch(x -> x.getStatusCatalogId().equals(firstStatusCatalogId) )){
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
        evaluations.stream().forEach(x-> x.setStatusCatalogId(statusCatalogId));
        evaluations = repository.saveAll(evaluations);
        return mapper.entityToDtoInfoList(evaluations);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION')")
    public EvaluationDTO.Info update(Long id, EvaluationDTO.Update dto) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(Evaluation, dto);
        Evaluation save = repository.save(Evaluation);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION')")
    public void delete(Long id) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(Evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

package com.nicico.evaluation.service;

import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.WorkSpaceDTO;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.WorkSpaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WorkSpaceService implements IWorkSpaceService {

    private final WorkSpaceMapper mapper;
    private final ICatalogService catalogService;
    private final IEvaluationService evaluationService;
    private final IMeritComponentService meritComponentService;
    private final IMeritComponentAuditService meritComponentAuditService;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    public List<WorkSpaceDTO.Info> workSpaceList() {
        List<CatalogDTO.Info> workSpaceCategoryList = catalogService.catalogByCatalogTypeCode("WorkSpaceCategory");
        return mapper.catalogDtoInfoToWorkSpaceDtoInfoList(workSpaceCategoryList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> workSpaceDetail(String code) {
        List<Long> meritComponentIds;
        switch (code) {
            case "workSpace-meritComponent-admin" -> meritComponentIds = meritComponentService.getAdminWorkInWorkSpace();
            case "workSpace-meritComponent-expert" -> meritComponentIds = meritComponentAuditService.getExpertWorkInWorkSpace();
            default -> meritComponentIds = null;
        }
        return meritComponentIds;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkSpaceDTO.Info> workSpaceAlarm(List<String> workSpaceCodeList) {
        List<CatalogDTO.Info> workSpaceCategoryList = new ArrayList<>();
        workSpaceCodeList.forEach(item -> workSpaceCategoryList.add(catalogService.getInfoByCode(item)));
        return mapper.catalogDtoInfoToWorkSpaceDtoInfoList(workSpaceCategoryList);
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationDTO.SpecResponse evaluationPeriodListByUser(int count, int startIndex) {
        String userNationalCode = SecurityUtil.getNationalCode();
        Long finalizedStatusCatalogId = catalogService.getByCode("Finalized").getId();
        return evaluationService.getAllByAssessNationalCodeAndStatusCatalogId(userNationalCode, finalizedStatusCatalogId, count, startIndex);
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationDTO.EvaluationAverageScoreData evaluationAverageScoreDataByUser(Long evaluationPeriodId) {
        String userNationalCode = SecurityUtil.getNationalCode();
        return evaluationService.getEvaluationAverageScoreDataByAssessNationalCodeAndEvaluationPeriodId(userNationalCode, evaluationPeriodId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO.MostParticipationInFinalizedEvaluation> mostParticipationPerOmoor(Long evaluationPeriodId) {
        Long finalizedStatusCatalogId = catalogService.getByCode("Finalized").getId();
        return evaluationService.mostParticipationInFinalizedEvaluationPerOmoor(evaluationPeriodId, finalizedStatusCatalogId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO.AverageWeightDTO> getFinalizedAverageByGradeAndPeriodEvaluation(Long periodId) {
        return evaluationService.getFinalizedAverageByGradeAndPeriodEvaluation(periodId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO.BestAssessAverageScoreDTO> getBestAssessesByOmoor(int count, int startIndex, Long periodId) {
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        return evaluationService.getBestAssessesByOmoor(pageable.getPageSize(), pageable.getPageNumber(), periodId);
    }
}

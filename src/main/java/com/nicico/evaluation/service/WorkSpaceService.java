package com.nicico.evaluation.service;

import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.dto.WorkSpaceDTO;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.WorkSpaceMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.nicico.evaluation.utility.EvaluationConstant.FINALIZED;
import static com.nicico.evaluation.utility.EvaluationConstant.PERIOD_FINALIZED;

@RequiredArgsConstructor
@Service
public class WorkSpaceService implements IWorkSpaceService {

    private final WorkSpaceMapper mapper;
    private final ModelMapper modelMapper;
    private final PageableMapper pageableMapper;
    private final ICatalogService catalogService;
    private final IEvaluationService evaluationService;
    private final IEvaluationPeriodService evaluationPeriodService;
    private final IMeritComponentService meritComponentService;
    private final IMeritComponentAuditService meritComponentAuditService;

    @Override
    @Transactional(readOnly = true)
    public List<WorkSpaceDTO.Info> workSpaceList() {
        List<CatalogDTO.Info> workSpaceCategoryList = catalogService.catalogByCatalogTypeCode("WorkSpaceCategory");
        return mapper.catalogDtoInfoToWorkSpaceDtoInfoList(workSpaceCategoryList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> workSpaceDetail(String code) {
        List<Long> objectIds;
        switch (code) {
            case "workSpace-meritComponent-admin" -> objectIds = meritComponentService.getAdminWorkInWorkSpace();
            case "workSpace-meritComponent-expert" -> objectIds = meritComponentAuditService.getExpertWorkInWorkSpace();
            case "workSpace-evaluation-assessor" -> objectIds = evaluationService.getAssessorWorkInWorkSpace();
            default -> objectIds = null;
        }
        return objectIds;
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
    public List<WorkSpaceDTO.Info> workSpaceAlarmNotification(List<String> workSpaceCodeList, String token) {
        List<WorkSpaceDTO.Info> workSpaceDTOList = new ArrayList<>();
        List<CatalogDTO.Info> workSpaceCategoryList = new ArrayList<>();
        workSpaceCodeList.forEach(item -> workSpaceCategoryList.add(catalogService.getInfoByCode(item)));
        workSpaceCategoryList.forEach(item -> {
            Integer number;
            switch (item.getCode()) {
                case "workSpace-meritComponent-admin" -> number = meritComponentService.getNumberOfAdminWorkInWorkSpace();
                case "workSpace-meritComponent-expert" -> number  = meritComponentAuditService.getNumberOfExpertWorkInWorkSpaceNotification(token);
                case "workSpace-evaluation-assessor" -> number  = evaluationService.getNumberOfAssessorWorkInWorkSpaceNotification(token);
                default -> number = null;
            }
            WorkSpaceDTO.Info info = modelMapper.map(item, WorkSpaceDTO.Info.class);
            info.setNumber(number);
            workSpaceDTOList.add(info);
        });
        return workSpaceDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationPeriodDTO.Info> evaluationPeriodListByUser(int count, int startIndex) {
        String userNationalCode = SecurityUtil.getNationalCode();
        Long finalizedStatusCatalogId = catalogService.getByCode(FINALIZED).getId();
        Long periodStatusId = catalogService.getByCode(PERIOD_FINALIZED).getId();
        return evaluationPeriodService.getAllByAssessNationalCodeAndStatusCatalogId(userNationalCode, finalizedStatusCatalogId, periodStatusId, count, startIndex);
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

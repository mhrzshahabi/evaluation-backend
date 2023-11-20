package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationGeneralReportDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationGeneralReportService;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.mapper.EvaluationViewGeneralReportMapper;
import com.nicico.evaluation.repository.EvaluationViewGeneralReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nicico.evaluation.utility.EvaluationConstant.FINALIZED;

@RequiredArgsConstructor
@Slf4j
@Service
public class EvaluationGeneralReportService implements IEvaluationGeneralReportService {

    private final EvaluationViewGeneralReportMapper mapper;
    private final EvaluationViewGeneralReportRepository repository;
    private final PageableMapper pageableMapper;
    private final EntityManager entityManager;
    private final IOrganizationTreeService organizationTreeService;
    private final ICatalogService catalogService;

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchByParent(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        List<String> postCodeList = organizationTreeService.getByParentNationalCode();

        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq assessorPostCodeCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.inSet)
                .setFieldName("assessPostCode")
                .setValue(!postCodeList.isEmpty() ? postCodeList : "0");

        final SearchDTO.CriteriaRq statusCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.equals)
                .setFieldName("statusCatalogId")
                .setValue(catalogService.getByCode(FINALIZED).getId());

        criteriaRqList.add(assessorPostCodeCriteriaRq);
        criteriaRqList.add(statusCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchEvaluationComprehensive(SearchDTO.SearchRq request, int count, int startIndex) {

        List<EvaluationGeneralReportDTO.Info> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getWhereClause(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        String query = getSubAssessorQuery(whereClause, pageable);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setInfoList(infoList, resultList);
            List totalResultList = entityManager.createNativeQuery(getTotalCountSubAssessorQuery(String.valueOf(whereClause))).getResultList();
            Long totalCount = !totalResultList.isEmpty() ? Long.parseLong(totalResultList.get(0).toString()) : 0;
            SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
            return searchRs;
        } else {
            SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(new ArrayList<>());
            searchRs.setTotalCount(0L);
            return searchRs;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchByPermission(SearchDTO.SearchRq request, int count, int startIndex) throws IllegalAccessException, NoSuchFieldException {

        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq statusCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.equals)
                .setFieldName("statusCatalogId")
                .setValue(catalogService.getByCode(FINALIZED).getId());

        criteriaRqList.add(statusCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        if (SecurityUtil.isAdmin())
            return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_GENERAL_REPORT_FIRST_LEVEL")))
            this.searchByParent(request);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_GENERAL_REPORT_LAST_LEVEL")))
            this.searchEvaluationComprehensive(request, count, startIndex);
        return null;
    }

    private List<EvaluationGeneralReportDTO.Info> setInfoList(List<EvaluationGeneralReportDTO.Info> infoList, List<?> resultList) {
        if (resultList != null) {
            for (Object evaluation : resultList) {
                Object[] eval = (Object[]) evaluation;
                EvaluationGeneralReportDTO.Info evaluationInfo = new EvaluationGeneralReportDTO.Info();
                evaluationInfo.setId(eval[0] == null ? null : Long.parseLong(eval[0].toString()));
                evaluationInfo.setAssessPostCode(eval[2] == null ? null : (eval[2].toString()));
                evaluationInfo.setEvaluationPeriodId(eval[5] == null ? null : (Long.parseLong(eval[5].toString())));
                evaluationInfo.setAverageScore(eval[6] == null ? null : Long.parseLong(eval[6].toString()));
                evaluationInfo.setAssessFullName(eval[8] == null ? null : (eval[8].toString()));
                evaluationInfo.setGhesmatTitle(eval[10] == null ? null : (eval[10].toString()));
                evaluationInfo.setMoavenatTitle(eval[11] == null ? null : (eval[11].toString()));
                evaluationInfo.setMojtamaTitle(eval[12] == null ? null : (eval[12].toString()));
                evaluationInfo.setOmoorTitle(eval[13] == null ? null : (eval[13].toString()));
                evaluationInfo.setCostCenterCode(eval[15] == null ? null : (eval[15].toString()));
                evaluationInfo.setCostCenterTitle(eval[16] == null ? null : (eval[16].toString()));
                evaluationInfo.setPersonnelCode(eval[17] == null ? null : (eval[17].toString()));
                evaluationInfo.setAvgBehavioral(eval[18] == null ? null : (eval[18].toString()));
                evaluationInfo.setWeightBehavioral(eval[19] == null ? null : (eval[19].toString()));
                evaluationInfo.setAvgDevelopment(eval[20] == null ? null : (eval[20].toString()));
                evaluationInfo.setWeightDevelopment(eval[21] == null ? null : (eval[21].toString()));
                evaluationInfo.setAvgOperational(eval[22] == null ? null : (eval[22].toString()));
                evaluationInfo.setWeightOperational(eval[23] == null ? null : (eval[23].toString()));
                evaluationInfo.setCountItem(eval[24] == null ? null : (Long.parseLong(eval[24].toString())));
                infoList.add(evaluationInfo);
            }
        }
        return infoList;
    }

    private String getSubAssessorQuery(String whereClause, Pageable pageable) {
        if (Objects.nonNull(pageable))
            whereClause = MessageFormat.format("{0}{1}", whereClause, String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize()));
        return String.format(" SELECT  " +
                        "                 eval.* " +
                        "            FROM  " +
                        "                view_evaluation_general_report  eval" +
                        "            WHERE  " +
                        "                c_assessor_post_code IN (  " +
                        "                    SELECT  " +
                        "                        *  " +
                        "                    FROM  " +
                        "                        (  " +
                        "                            SELECT  " +
                        "                                post_code  " +
                        "                            FROM  " +
                        "                                view_organization_tree  " +
                        "                            START WITH  " +
                        "                                post_code = (  " +
                        "                                    SELECT  " +
                        "                                        post_code  " +
                        "                                    FROM  " +
                        "                                        view_organization_tree  " +
                        "                                    WHERE  " +
                        "                                        national_code = %s  " +
                        "                                )  " +
                        "                            CONNECT BY  " +
                        "                                PRIOR post_code = post_parent_code  " +
                        "                            ORDER BY  " +
                        "                                level  " +
                        "                        )  " +
                        "                )" +
                        "               %s "
                , SecurityUtil.getNationalCode(), whereClause);
    }

    private String getTotalCountSubAssessorQuery(String whereClause) {

        return String.format(" SELECT  " +
                "                 count(eval.id) " +
                "            FROM  " +
                "                view_evaluation_general_report  eval" +
                "               join tbl_catalog             catalog ON catalog.id = eval.status_catalog_id " +
                "            WHERE  " +
                "                c_assessor_post_code IN (  " +
                "                    SELECT  " +
                "                        *  " +
                "                    FROM  " +
                "                        (  " +
                "                            SELECT  " +
                "                                post_code  " +
                "                            FROM  " +
                "                                view_organization_tree  " +
                "                            START WITH  " +
                "                                post_code = (  " +
                "                                    SELECT  " +
                "                                        post_code  " +
                "                                    FROM  " +
                "                                        view_organization_tree  " +
                "                                    WHERE  " +
                "                                        national_code = %s  " +
                "                                )  " +
                "                            CONNECT BY  " +
                "                                PRIOR post_code = post_parent_code  " +
                "                            ORDER BY  " +
                "                                level  " +
                "                        )  " +
                "                )" +
                "               %s ", SecurityUtil.getNationalCode(), whereClause);
    }

    private StringBuilder getWhereClause(SearchDTO.SearchRq request) {
        StringBuilder whereClause = new StringBuilder();
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
            request.getCriteria().getCriteria().forEach(criteria -> {
                switch (criteria.getFieldName()) {
                    case "assessFullName" -> whereClause.append(" and ").append("eval.c_assess_full_name").append(" like '%").append(criteria.getValue().toString()).append("%'");
                    case "assessPostCode" -> whereClause.append(" and ").append("eval.c_assess_post_code").append(" like '%").append(criteria.getValue()).append("%'");
                    case "assessPostTitle" -> whereClause.append(" and ").append("eval.post_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "evaluationPeriod.id" -> whereClause.append(" and ").append("evalPeriod.id").append(" like '%").append(criteria.getValue()).append("%'");
                    case "averageScore" -> whereClause.append(" and ").append("eval.average_score").append(" like '%").append(criteria.getValue()).append("%'");
                    case "postGradeTitle" -> whereClause.append(" and ").append("eval.post_grade_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "mojtamaTitle" -> whereClause.append(" and ").append("eval.mojtama_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "moavenatTitle" -> whereClause.append(" and ").append("eval.moavenat_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "omoorTitle" -> whereClause.append(" and ").append("eval.omoor_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "ghesmatTitle" -> whereClause.append(" and ").append("eval.ghesmat_title").append(" like '%").append(criteria.getValue()).append("%'");
                }
            });
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }

}

package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationDTO;
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

        criteriaRqList.add(assessorPostCodeCriteriaRq);
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
    public SearchDTO.SearchRs<EvaluationGeneralReportDTO.DetailInfo> searchEvaluationGeneralReportDetail(SearchDTO.SearchRq request, int count, int startIndex) {

        List<EvaluationGeneralReportDTO.DetailInfo> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getGeneralReportDetailWhereClause(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        String query = getGeneralReportDetailQuery(whereClause, pageable);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setGeneralReportDetailInfoList(infoList, resultList);
            List totalResultList = entityManager.createNativeQuery(getTotalEvaluationGeneralReportQuery(String.valueOf(whereClause))).getResultList();
            Long totalCount = !totalResultList.isEmpty() ? Long.parseLong(totalResultList.get(0).toString()) : 0;
            SearchDTO.SearchRs<EvaluationGeneralReportDTO.DetailInfo> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
            return searchRs;
        } else {
            SearchDTO.SearchRs<EvaluationGeneralReportDTO.DetailInfo> searchRs = new SearchDTO.SearchRs<>();
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
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
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

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchByCostCenter(SearchDTO.SearchRq request, int count, int startIndex) {

        Long statusCatalogId = catalogService.getByCode(FINALIZED).getId();
        if (SecurityUtil.isAdmin())
            return this.searchAdminByCostCenter(request, statusCatalogId, count, startIndex);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_COST_CENTER_REPORT_FIRST_LEVEL")))
            return this.searchFirstLevelByCostCenter(request, statusCatalogId, count, startIndex);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_COST_CENTER_REPORT_LAST_LEVEL")))
            return this.searchLastLevelByCostCenter(request, statusCatalogId, count, startIndex);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchAdminByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex) {

        List<EvaluationDTO.CostCenterInfo> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getWhereClauseByCostCenter(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        String query = getAdminQueryByCostCenter(whereClause, statusCatalogId, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setCostCenterInfoList(infoList, resultList);
            List totalResultList = entityManager.createNativeQuery(getTotalCountAdminQueryByCostCenter(whereClause, statusCatalogId)).getResultList();
            Long totalCount = !totalResultList.isEmpty() ? Long.parseLong(totalResultList.get(0).toString()) : 0;
            SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
            return searchRs;
        } else {
            SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(new ArrayList<>());
            searchRs.setTotalCount(0L);
            return searchRs;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchFirstLevelByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex) {

        List<EvaluationDTO.CostCenterInfo> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getWhereClauseByCostCenter(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        String query = getSubAssessorQuery(whereClause, pageable);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setCostCenterInfoList(infoList, resultList);
            List totalResultList = entityManager.createNativeQuery(getTotalCountSubAssessorQuery(String.valueOf(whereClause))).getResultList();
            Long totalCount = !totalResultList.isEmpty() ? Long.parseLong(totalResultList.get(0).toString()) : 0;
            SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
            return searchRs;
        } else {
            SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(new ArrayList<>());
            searchRs.setTotalCount(0L);
            return searchRs;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchLastLevelByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex) {

        List<EvaluationDTO.CostCenterInfo> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getWhereClauseByCostCenter(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        String query = getSubAssessorQuery(whereClause, pageable);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setCostCenterInfoList(infoList, resultList);
            List totalResultList = entityManager.createNativeQuery(getTotalCountSubAssessorQuery(String.valueOf(whereClause))).getResultList();
            Long totalCount = !totalResultList.isEmpty() ? Long.parseLong(totalResultList.get(0).toString()) : 0;
            SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
            return searchRs;
        } else {
            SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(new ArrayList<>());
            searchRs.setTotalCount(0L);
            return searchRs;
        }
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

    private List<EvaluationGeneralReportDTO.DetailInfo> setGeneralReportDetailInfoList(List<EvaluationGeneralReportDTO.DetailInfo> infoList, List<?> resultList) {
        if (resultList != null) {
            for (Object evaluation : resultList) {
                Object[] eval = (Object[]) evaluation;
                EvaluationGeneralReportDTO.DetailInfo evaluationInfo = new EvaluationGeneralReportDTO.DetailInfo();
                evaluationInfo.setMeritCode(eval[0] == null ? null : (eval[0].toString()));
                evaluationInfo.setMeritTitle(eval[1] == null ? null : (eval[1].toString()));
                evaluationInfo.setEffectiveScore(eval[2] == null ? null : (eval[2].toString()));
                evaluationInfo.setDescription(eval[3] == null ? null : (eval[3].toString()));
                evaluationInfo.setKpiTitle(eval[4] == null ? null : (eval[4].toString()));
                evaluationInfo.setMeritWeight(eval[5] == null ? null : (eval[5].toString()));
                infoList.add(evaluationInfo);
            }
        }
        return infoList;
    }

    private List<EvaluationDTO.CostCenterInfo> setCostCenterInfoList(List<EvaluationDTO.CostCenterInfo> infoList, List<?> resultList) {
        if (resultList != null) {
            for (Object evaluation : resultList) {
                Object[] eval = (Object[]) evaluation;
                EvaluationDTO.CostCenterInfo costCenterInfo = new EvaluationDTO.CostCenterInfo();
                costCenterInfo.setCostCenterCode(eval[0] == null ? null : eval[0].toString());
                costCenterInfo.setCostCenterTitle(eval[1] == null ? null : eval[1].toString());
                costCenterInfo.setEvaluationPeriodId(eval[2] == null ? null : (Long.parseLong(eval[2].toString())));
                costCenterInfo.setPersonCount(eval[3] == null ? null : (Integer.parseInt(eval[3].toString())));
                costCenterInfo.setAverageBehavioral(eval[4] == null ? null : eval[4].toString());
                costCenterInfo.setAverageDevelopment(eval[5] == null ? null : eval[5].toString());
                costCenterInfo.setAverageOperational(eval[6] == null ? null : eval[6].toString());
                costCenterInfo.setAverageScore(eval[7] == null ? null : eval[7].toString());
                infoList.add(costCenterInfo);
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

    private String getGeneralReportDetailQuery(String whereClause, Pageable pageable) {
        if (Objects.nonNull(pageable))
            whereClause = MessageFormat.format("{0}{1}", whereClause, String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize()));
        return String.format(" " +
                        " SELECT " +
                        " distinct " +
                        "    merit.c_code, " +
                        "    merit.c_title, " +
                        "    catalog.c_value, " +
                        "    evalItem.c_description, " +
                        "    nvl( kpiType.c_title,kpiType1.c_title) kpiTitle, " +
                        "    nvl(groupTypeMerit.N_WEIGHT,postMerit.N_WEIGHT) meritWeight " +
                        " FROM " +
                        "    tbl_evaluation_item        evalItem " +
                        "    JOIN tbl_merit_component_aud   merit ON merit.id =  evalItem.merit_component_audit_id " +
                        "                                          AND merit.rev =  evalItem.merit_component_audit_rev " +
                        "    JOIN tbl_catalog   catalog ON catalog.id =  evalItem.questionnaire_answer_catalog_id " +
                        "    left join tbl_group_type_merit groupTypeMerit on  groupTypeMerit.id =  evalItem.group_type_merit_id " +
                        "    left join tbl_group_type groupType on groupType.id = groupTypeMerit.GROUP_TYPE_ID " +
                        "    left join tbl_kpi_type kpiType on kpiType.id = groupType.KPI_TYPE_ID " +
                        "    left join tbl_post_merit_component   postMerit on postMerit.id =  evalItem.POST_MERIT_ID " +
                        "    left JOIN tbl_merit_component_type   meritType ON meritType.merit_component_id = postMerit.merit_component_id " +
                        "    left JOIN tbl_group_type             groupType1 ON groupType1.kpi_type_id = meritType.kpi_type_id " +
                        "    left JOIN tbl_group                  group1 ON group1.id = groupType1.group_id " +
                        "    left JOIN tbl_group_grade            groupGrade ON groupGrade.group_id = group1.id " +
                        "    left JOIN view_grade                 grade ON grade.id = groupGrade.grade_id " +
                        "    left JOIN view_post                  post ON post.post_grade_code = grade.c_post_grade_code " +
                        "    left JOIN tbl_kpi_type              kpiType1 ON kpiType1.id = meritType.kpi_type_id" +
                        "     Where 1 = 1  %s "
                , whereClause);
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

    private String getTotalEvaluationGeneralReportQuery(String whereClause) {

        return String.format("SELECT COUNT(*) " +
                        "FROM(" +
                        " SELECT " +
                        " distinct " +
                        "    evalItem.id " +
                        " FROM " +
                        "    tbl_evaluation_item        evalItem " +
                        "    JOIN tbl_merit_component_aud   merit ON merit.id =  evalItem.merit_component_audit_id " +
                        "                                          AND merit.rev =  evalItem.merit_component_audit_rev " +
                        "    JOIN tbl_catalog   catalog ON catalog.id =  evalItem.questionnaire_answer_catalog_id " +
                        "    left join tbl_group_type_merit groupTypeMerit on  groupTypeMerit.id =  evalItem.group_type_merit_id " +
                        "    left join tbl_group_type groupType on groupType.id = groupTypeMerit.GROUP_TYPE_ID " +
                        "    left join tbl_kpi_type kpiType on kpiType.id = groupType.KPI_TYPE_ID " +
                        "    left join tbl_post_merit_component   postMerit on postMerit.id =  evalItem.POST_MERIT_ID " +
                        "    left JOIN tbl_merit_component_type   meritType ON meritType.merit_component_id = postMerit.merit_component_id " +
                        "    left JOIN tbl_group_type             groupType1 ON groupType1.kpi_type_id = meritType.kpi_type_id " +
                        "    left JOIN tbl_group                  group1 ON group1.id = groupType1.group_id " +
                        "    left JOIN tbl_group_grade            groupGrade ON groupGrade.group_id = group1.id " +
                        "    left JOIN view_grade                 grade ON grade.id = groupGrade.grade_id " +
                        "    left JOIN view_post                  post ON post.post_grade_code = grade.c_post_grade_code " +
                        "    left JOIN tbl_kpi_type              kpiType1 ON kpiType1.id = meritType.kpi_type_id" +
                        "    Where 1 = 1  %s )"
                , whereClause);
    }

    private String getAdminQueryByCostCenter(String whereClause, Long statusCatalogId, int pageNumber, int pageSize) {
        return String.format("""
                        SELECT
                            evaluation.cost_center_code,
                            evaluation.cost_center_title,
                            evaluation.evaluation_period_id,
                            COUNT(DISTINCT evaluation.id)   AS person_count,
                            SUM(evaluation.avg_behavioral)  AS average_behavioral,
                            SUM(evaluation.avg_development) AS average_development,
                            SUM(evaluation.avg_operational) AS average_operational,
                            SUM(evaluation.average_score)   AS average_score
                        FROM
                            (
                                SELECT
                                    eval.*
                                FROM
                                    view_evaluation_general_report eval
                                WHERE
                                    eval.status_catalog_id = %s
                                    %s
                            ) evaluation
                        GROUP BY
                            evaluation.evaluation_period_id,
                            evaluation.cost_center_code,
                            evaluation.cost_center_title
                        OFFSET %s ROWS FETCH NEXT %s ROWS ONLY
                        """
                , statusCatalogId, whereClause, pageNumber, pageSize);
    }

    private String getTotalCountAdminQueryByCostCenter(String whereClause, Long statusCatalogId) {
        return String.format("""
                        SELECT
                            COUNT(DISTINCT cost_center_title)
                        FROM
                            (
                                SELECT
                                    evaluation.cost_center_code,
                                    evaluation.cost_center_title,
                                    evaluation.evaluation_period_id,
                                    COUNT(DISTINCT evaluation.id)   AS person_count,
                                    SUM(evaluation.avg_behavioral)  AS average_behavioral,
                                    SUM(evaluation.avg_development) AS average_development,
                                    SUM(evaluation.avg_operational) AS average_operational,
                                    SUM(evaluation.average_score)   AS average_score
                                FROM
                                    (
                                        SELECT
                                            eval.*
                                        FROM
                                            view_evaluation_general_report eval
                                        WHERE
                                            eval.status_catalog_id = %s
                                            %s
                                    ) evaluation
                                GROUP BY
                                    evaluation.evaluation_period_id,
                                    evaluation.cost_center_code,
                                    evaluation.cost_center_title
                            )
                        """
                , statusCatalogId, whereClause);
    }

    private StringBuilder getWhereClause(SearchDTO.SearchRq request) {
        StringBuilder whereClause = new StringBuilder();
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
            request.getCriteria().getCriteria().forEach(criteria -> {
                switch (criteria.getFieldName()) {
                    case "assessFullName" -> whereClause.append(" and ").append("eval.c_assess_full_name").append(" like '%").append(criteria.getValue().toString()).append("%'");
                    case "assessPostCode" -> whereClause.append(" and ").append("eval.c_assess_post_code").append(" like '%").append(criteria.getValue()).append("%'");
                    case "assessPostTitle" -> whereClause.append(" and ").append("eval.post_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "evaluationPeriodId" -> whereClause.append(" and ").append("eval.evaluation_period_id").append(" = ").append(criteria.getValue());
                    case "averageScore" -> whereClause.append(" and ").append("eval.average_score").append(" =").append(criteria.getValue());
                    case "postGradeTitle" -> whereClause.append(" and ").append("eval.post_grade_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "mojtamaTitle" -> whereClause.append(" and ").append("eval.mojtama_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "moavenatTitle" -> whereClause.append(" and ").append("eval.moavenat_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "omoorTitle" -> whereClause.append(" and ").append("eval.omoor_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "ghesmatTitle" -> whereClause.append(" and ").append("eval.ghesmat_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "statusCatalogId" -> whereClause.append(" and ").append("eval.status_catalog_id").append(" = ").append(criteria.getValue());
                    case "avgBehavioral" -> whereClause.append(" and ").append("eval.avg_behavioral").append(" like '%").append(criteria.getValue()).append("%'");
                    case "avgDevelopment" -> whereClause.append(" and ").append("eval.avg_development").append(" like '%").append(criteria.getValue()).append("%'");
                    case "avgOperational" -> whereClause.append(" and ").append("eval.avg_operational").append(" like '%").append(criteria.getValue()).append("%'");
                    case "weightBehavioral" -> whereClause.append(" and ").append("eval.weight_behavioral").append(" like '%").append(criteria.getValue()).append("%'");
                    case "weightDevelopment" -> whereClause.append(" and ").append("eval.weight_development").append(" like '%").append(criteria.getValue()).append("%'");
                    case "weightOperational" -> whereClause.append(" and ").append("eval.weight_operational").append(" like '%").append(criteria.getValue()).append("%'");
                }
            });
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }

    private StringBuilder getWhereClauseByCostCenter(SearchDTO.SearchRq request) {
        StringBuilder whereClause = new StringBuilder();
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
            request.getCriteria().getCriteria().forEach(criteria -> {
                if (Objects.nonNull(criteria.getFieldName())) {
                    switch (criteria.getFieldName()) {
                        case "costCenterCode" -> whereClause.append(" and ").append("eval.cost_center_code").append(" like '%").append(criteria.getValue()).append("%'");
                        case "costCenterTitle" -> whereClause.append(" and ").append("eval.cost_center_title").append(" like '%").append(criteria.getValue()).append("%'");
                        case "evaluationPeriodId" -> whereClause.append(" and ").append("eval.evaluation_period_id").append(" = ").append(criteria.getValue().toString());
//                        case "averageScore" -> whereClause.append(" and ").append("eval.average_score").append(" like '%").append(criteria.getValue()).append("%'");
                        default -> {
                        }
                    }
                }
            });
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }

    private StringBuilder getGeneralReportDetailWhereClause(SearchDTO.SearchRq request) {
        StringBuilder whereClause = new StringBuilder();
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
            request.getCriteria().getCriteria().forEach(criteria -> {
                switch (criteria.getFieldName()) {
                    case "evaluationId" -> whereClause.append(" and ").append("evalItem.evaluation_id").append(" = ").append(criteria.getValue().toString());
                    case "meritCode" -> whereClause.append(" and ").append("merit.c_code").append(" like '%").append(criteria.getValue().toString()).append("%'");
                    case "meritTitle" -> whereClause.append(" and ").append("merit.c_title").append(" like '%").append(criteria.getValue().toString()).append("%'");
                    case "effectiveScore" -> whereClause.append(" and ").append("catalog.c_value").append(" like '%").append(criteria.getValue().toString()).append("%'");
                    case "kpiTitle" -> whereClause.append(" and ").append("kpiTitle").append(" like '%").append(criteria.getValue().toString()).append("%'");
                    case "description" -> whereClause.append(" and ").append("evalItem.c_description").append(" like '%").append(criteria.getValue().toString()).append("%'");
                    case "meritWeight" -> whereClause.append(" and ").append("meritWeight").append(" like '%").append(criteria.getValue().toString()).append("%'");

                }
            });
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }
}

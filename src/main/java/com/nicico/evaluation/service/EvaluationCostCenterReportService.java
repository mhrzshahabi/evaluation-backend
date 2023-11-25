package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationCostCenterReportService;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.mapper.EvaluationCostCenterReportMapper;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.nicico.evaluation.utility.EvaluationConstant.FINALIZED;

@RequiredArgsConstructor
@Slf4j
@Service
public class EvaluationCostCenterReportService implements IEvaluationCostCenterReportService {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;
    private final PageableMapper pageableMapper;
    private final ICatalogService catalogService;
    private final EvaluationCostCenterReportMapper mapper;
    private final IOrganizationTreeService organizationTreeService;

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchByCostCenter(SearchDTO.SearchRq request, int count, int startIndex) {

        Long statusCatalogId = catalogService.getByCode(FINALIZED).getId();
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        if (SecurityUtil.isAdmin())
            return this.searchAdminByCostCenter(request, statusCatalogId, pageable);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_COST_CENTER_REPORT_LAST_LEVEL")))
            return this.searchLastLevelByCostCenter(request, statusCatalogId, pageable);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_COST_CENTER_REPORT_FIRST_LEVEL")))
            return this.searchFirstLevelByCostCenter(request, statusCatalogId, pageable);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchAdminByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, Pageable pageable) {

        List<EvaluationDTO.CostCenterInfo> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getWhereClauseByCostCenter(request, false));
        String outerWhereClause = String.valueOf(getOuterWhereClauseByCostCenter(request));
        String query = getAdminQueryByCostCenter(whereClause, outerWhereClause, statusCatalogId, pageable);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setCostCenterInfoList(infoList, resultList);
            String totalQuery = getAdminQueryByCostCenter(whereClause, outerWhereClause, statusCatalogId, null);
            Long totalCount = jdbcTemplate.queryForObject(MessageFormat.format(" Select count(*) from ({0})", totalQuery), Long.class);
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
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchFirstLevelByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, Pageable pageable) {

        List<EvaluationDTO.CostCenterInfo> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getWhereClauseByCostCenter(request, true));
        String outerWhereClause = String.valueOf(getOuterWhereClauseByCostCenter(request));
        String query = getAdminQueryByCostCenter(whereClause, outerWhereClause, statusCatalogId, pageable);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setCostCenterInfoList(infoList, resultList);
            String totalQuery = getAdminQueryByCostCenter(whereClause, outerWhereClause, statusCatalogId, null);
            Long totalCount = jdbcTemplate.queryForObject(MessageFormat.format(" Select count(*) from ({0})", totalQuery), Long.class);
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
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchLastLevelByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, Pageable pageable) {

        List<EvaluationDTO.CostCenterInfo> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getWhereClauseByCostCenter(request, false));
        String outerWhereClause = String.valueOf(getOuterWhereClauseByCostCenter(request));
        String query = getLastLevelQueryByCostCenter(whereClause, outerWhereClause, statusCatalogId, pageable);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setCostCenterInfoList(infoList, resultList);
            String totalQuery = getLastLevelQueryByCostCenter(whereClause, outerWhereClause, statusCatalogId, null);
            Long totalCount = jdbcTemplate.queryForObject(MessageFormat.format(" Select count(*) from ({0})", totalQuery), Long.class);
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
    public ResponseEntity<byte[]> exportExcelCostCenterReport(List<FilterDTO> criteria) {

        SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> costCenterInfoSearchRs = new SearchDTO.SearchRs<>();
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, null, null);
        Long statusCatalogId = catalogService.getByCode(FINALIZED).getId();
        if (SecurityUtil.isAdmin())
            costCenterInfoSearchRs = this.searchAdminByCostCenter(request, statusCatalogId, null);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_COST_CENTER_REPORT_LAST_LEVEL")))
            costCenterInfoSearchRs = this.searchLastLevelByCostCenter(request, statusCatalogId, null);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_COST_CENTER_REPORT_FIRST_LEVEL")))
            costCenterInfoSearchRs = this.searchFirstLevelByCostCenter(request, statusCatalogId, null);

        byte[] body = BaseService.exportExcelByList(mapper.infoToDtoExcelList(costCenterInfoSearchRs.getList()), null, "گزارش ارزیابی سازمان");
        ExcelGenerator.ExcelDownload excelDownload = new ExcelGenerator.ExcelDownload(body);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                .body(excelDownload.getContent());
    }

    private StringBuilder getWhereClauseByCostCenter(SearchDTO.SearchRq request, boolean firstLevel) {
        StringBuilder whereClause = new StringBuilder();
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
            request.getCriteria().getCriteria().forEach(criteria -> {
                if (Objects.nonNull(criteria.getFieldName())) {
                    switch (criteria.getFieldName()) {
                        case "costCenterCode" -> whereClause.append(" and ").append("eval.cost_center_code").append(" like '%").append(criteria.getValue()).append("%'");
                        case "costCenterTitle" -> whereClause.append(" and ").append("eval.cost_center_title").append(" like '%").append(criteria.getValue()).append("%'");
                        case "evaluationPeriodId" -> whereClause.append(" and ").append("eval.evaluation_period_id").append(" = ").append(criteria.getValue().toString());
                        default -> {
                        }
                    }
                }
            });
        if (firstLevel) {
            List<String> postCodeList = organizationTreeService.getByParentNationalCode();
            whereClause.append(" and ").append("eval.c_assess_post_code").append(" in ").append(!postCodeList.isEmpty() ? "(" +
                    postCodeList.stream().map(item -> "'" + item + "'").collect(Collectors.joining(", ")) + ")" : "('0')");
        }
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }

    private StringBuilder getOuterWhereClauseByCostCenter(SearchDTO.SearchRq request) {
        StringBuilder whereClause = new StringBuilder();
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
            request.getCriteria().getCriteria().forEach(criteria -> {
                if (Objects.nonNull(criteria.getFieldName())) {
                    switch (criteria.getFieldName()) {
                        case "personCount" -> whereClause.append(" and ").append("person_count").append(" = ").append(criteria.getValue().toString());
                        case "averageBehavioral" -> whereClause.append(" and ").append("average_behavioral").append(" = ").append(criteria.getValue().toString());
                        case "averageDevelopment" -> whereClause.append(" and ").append("average_development").append(" = ").append(criteria.getValue().toString());
                        case "averageOperational" -> whereClause.append(" and ").append("average_operational").append(" = ").append(criteria.getValue().toString());
                        case "averageScore" -> whereClause.append(" and ").append("average_score").append(" = ").append(criteria.getValue().toString());
                        default -> {
                        }
                    }
                }
            });
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }

    private String getAdminQueryByCostCenter(String whereClause, String outerWhereClause, Long statusCatalogId, Pageable pageable) {
        if (Objects.nonNull(pageable))
            outerWhereClause = MessageFormat.format("{0}{1}", outerWhereClause, String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize()));
        return String.format("""
                        SELECT
                            *
                        FROM
                            (
                                SELECT
                                    evaluation.cost_center_code,
                                    evaluation.cost_center_title,
                                    evaluation.evaluation_period_id,
                                    COUNT(DISTINCT evaluation.id)                                   AS person_count,
                                    SUM(evaluation.avg_behavioral) / COUNT(DISTINCT evaluation.id)  AS average_behavioral,
                                    SUM(evaluation.avg_development) / COUNT(DISTINCT evaluation.id) AS average_development,
                                    SUM(evaluation.avg_operational) / COUNT(DISTINCT evaluation.id) AS average_operational,
                                    SUM(evaluation.average_score) / COUNT(DISTINCT evaluation.id)   AS average_score
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
                        WHERE
                            1 = 1
                            %s
                        """
                , statusCatalogId, whereClause, outerWhereClause);
    }

    private String getLastLevelQueryByCostCenter(String whereClause, String outerWhereClause, Long statusCatalogId, Pageable pageable) {
        if (Objects.nonNull(pageable))
            outerWhereClause = MessageFormat.format("{0}{1}", outerWhereClause, String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize()));
        return String.format("""
                        SELECT
                            *
                        FROM
                            (
                                SELECT
                                    evaluation.cost_center_code,
                                    evaluation.cost_center_title,
                                    evaluation.evaluation_period_id,
                                    COUNT(DISTINCT evaluation.id)                                   AS person_count,
                                    SUM(evaluation.avg_behavioral) / COUNT(DISTINCT evaluation.id)  AS average_behavioral,
                                    SUM(evaluation.avg_development) / COUNT(DISTINCT evaluation.id) AS average_development,
                                    SUM(evaluation.avg_operational) / COUNT(DISTINCT evaluation.id) AS average_operational,
                                    SUM(evaluation.average_score) / COUNT(DISTINCT evaluation.id)   AS average_score
                                FROM
                                    (
                                        SELECT
                                            eval.*
                                        FROM
                                            view_evaluation_general_report eval
                                        WHERE
                                            c_assessor_post_code IN (
                                                SELECT
                                                    *
                                                FROM
                                                    (
                                                        SELECT
                                                            post_code
                                                        FROM
                                                            view_organization_tree
                                                        START WITH
                                                            post_code = (
                                                                SELECT
                                                                    post_code
                                                                FROM
                                                                    view_organization_tree
                                                                WHERE
                                                                    national_code = %s
                                                            )
                                                        CONNECT BY
                                                            PRIOR post_code = post_parent_code
                                                        ORDER BY
                                                            level
                                                    )
                                            )
                                            AND eval.status_catalog_id = %s
                                            %s
                                    ) evaluation
                                GROUP BY
                                    evaluation.evaluation_period_id,
                                    evaluation.cost_center_code,
                                    evaluation.cost_center_title
                            )
                        WHERE
                            1 = 1
                            %s
                        """
                , SecurityUtil.getNationalCode(), statusCatalogId, whereClause, outerWhereClause);
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
}

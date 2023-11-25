package com.nicico.evaluation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationGeneralReportDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationGeneralReportService;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.mapper.EvaluationViewGeneralReportMapper;
import com.nicico.evaluation.repository.EvaluationViewGeneralReportRepository;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

import static com.nicico.evaluation.utility.EvaluationConstant.FINALIZED;

@RequiredArgsConstructor
@Slf4j
@Service
public class EvaluationGeneralReportService implements IEvaluationGeneralReportService {

    private final ModelMapper modelMapper;
    private final JdbcTemplate jdbcTemplate;
    private final PageableMapper pageableMapper;
    private final ICatalogService catalogService;
    private final EvaluationViewGeneralReportMapper mapper;
    private final EvaluationViewGeneralReportRepository repository;
    private final IOrganizationTreeService organizationTreeService;
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

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

        String whereClause = String.valueOf(getGeneralReportWhereClause(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        String query = getGeneralReportQuery(whereClause, pageable);
        List<Map<String, Object>> resultList = parameterJdbcTemplate.queryForList(query, new HashMap<>());
        List<EvaluationGeneralReportDTO.Info> infoList = modelMapper.map(resultList, new TypeReference<List<EvaluationGeneralReportDTO.Info>>() {
        }.getType());
        SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchRs = new SearchDTO.SearchRs<>();
        searchRs.setList(new ArrayList<>());
        searchRs.setTotalCount(0L);
        if (!resultList.isEmpty()) {
            String totalQuery = getGeneralReportQuery(whereClause, null);
            Long totalCount = jdbcTemplate.queryForObject(MessageFormat.format(" Select count(*) from ({0})", totalQuery), Long.class);
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
            return searchRs;
        }
        return searchRs;
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationGeneralReportDTO.DetailInfo> searchEvaluationGeneralReportDetail(SearchDTO.SearchRq request, int count, int startIndex) {

        String whereClause = String.valueOf(getGeneralReportDetailWhereClause(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        String query = getGeneralReportDetailQuery(whereClause, pageable);
        List<Map<String, Object>> resultList = parameterJdbcTemplate.queryForList(query, new MapSqlParameterSource());
        List<EvaluationGeneralReportDTO.DetailInfo> infoList = modelMapper.map(resultList, new TypeReference<List<EvaluationGeneralReportDTO.DetailInfo>>() {
        }.getType());
        SearchDTO.SearchRs<EvaluationGeneralReportDTO.DetailInfo> searchRs = new SearchDTO.SearchRs<>();
        searchRs.setList(new ArrayList<>());
        if (!resultList.isEmpty()) {
            String totalQuery = getGeneralReportDetailQuery(whereClause, null);
            Long totalCount = jdbcTemplate.queryForObject(MessageFormat.format(" Select count(*) from ({0})", totalQuery), Long.class);
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
        }
        return searchRs;
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
            criteriaRqList.addAll(request.getCriteria().getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);
        SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> infoSearchRs = new SearchDTO.SearchRs<>();
        if (SecurityUtil.isAdmin())
            infoSearchRs = BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_GENERAL_REPORT_LAST_LEVEL")))
            infoSearchRs = this.searchEvaluationComprehensive(request, count, startIndex);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_GENERAL_REPORT_FIRST_LEVEL")))
            infoSearchRs = this.searchByParent(request);
        return infoSearchRs;
    }

    @SneakyThrows
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadExcel(List<FilterDTO> criteria) {

        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setField("statusCatalogId");
        filterDTO.setType("null");
        filterDTO.setOperator(String.valueOf(EOperator.equals));
        filterDTO.setValues(Collections.singletonList(catalogService.getByCode(FINALIZED).getId()));
        criteria.add(filterDTO);
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, null, null);
        SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchRsData = new SearchDTO.SearchRs<>();
        if (SecurityUtil.isAdmin())
            searchRsData = BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_GENERAL_REPORT_LAST_LEVEL")))
            searchRsData = this.searchEvaluationComprehensive(request, 0, Integer.MAX_VALUE);
        else if (Boolean.TRUE.equals(SecurityUtil.hasAuthority("R_EVALUATION_GENERAL_REPORT_FIRST_LEVEL")))
            searchRsData = this.searchByParent(request);

        if (!searchRsData.getList().isEmpty())
            return generateReportExcel(searchRsData.getList(), request);
        else
            return ResponseEntity.noContent().build();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @SneakyThrows
    @NotNull
    private ResponseEntity<byte[]> generateReportExcel(List<EvaluationGeneralReportDTO.Info> masterData, SearchDTO.SearchRq request) {

        List<EvaluationGeneralReportDTO.Excel> detailList = getDetailInfoExcel(masterData, request);
        List<EvaluationGeneralReportDTO.Excel> dataList = detailList.stream().sorted(Comparator.comparing(EvaluationGeneralReportDTO.Excel::getEvalId)).toList();
        dataList.forEach(detail -> {
            if (masterData.stream().anyMatch(q -> Objects.equals(q.getId(), detail.getEvalId()))) {
                EvaluationGeneralReportDTO.Info masterInfo = masterData.stream().filter(q -> Objects.equals(q.getId(), detail.getEvalId())).findFirst().get();
                mapper.setExcelDto(detail, masterInfo);
            }
        });
        byte[] body = BaseService.exportExcelByList(dataList, "گزارش مولفه های ارزیابی", "گزارش مولفه های ارزیابی");
        ExcelGenerator.ExcelDownload excelDownload = new ExcelGenerator.ExcelDownload(body);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                .body(excelDownload.getContent());
    }

    private List<EvaluationGeneralReportDTO.Excel> getDetailInfoExcel(List<EvaluationGeneralReportDTO.Info> masterData, SearchDTO.SearchRq request) {
        List<Long> evaluationIds = masterData.stream().map(EvaluationGeneralReportDTO.Info::getId).toList();
        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq evalIdsCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.inSet)
                .setFieldName("evaluationIds")
                .setValue(evaluationIds);

        criteriaRqList.add(evalIdsCriteriaRq);
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
            criteriaRqList.addAll(request.getCriteria().getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        String whereClause = String.valueOf(getGeneralReportDetailWhereClause(request));
        String query = getGeneralReportDetailQuery(whereClause, null);
        List<Map<String, Object>> resultList = parameterJdbcTemplate.queryForList(query, new HashMap<>());
        return modelMapper.map(resultList, new TypeReference<List<EvaluationGeneralReportDTO.Excel>>() {
        }.getType());
    }

    private String getGeneralReportQuery(String whereClause, Pageable pageable) {
        if (Objects.nonNull(pageable))
            whereClause = MessageFormat.format("{0}{1}", whereClause, String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize()));
        return String.format(" SELECT  " +
                        "           id      \"id\"  , " +
                        "           c_assess_full_name     \"assessFullName\"  , " +
                        "           C_ASSESS_POST_CODE    \"assessPostCode\"  , " +
                        "           personnel_code \"personnelCode\"   , " +
                        "           cost_center_code  \"costCenterCode\"  , " +
                        "           cost_center_title \"costCenterTitle\" , " +
                        "           mojtama_title \"mojtamaTitle\"    , " +
                        "           moavenat_title \"moavenatTitle\"   , " +
                        "           omoor_title \"omoorTitle\"  , " +
                        "           ghesmat_title \"ghesmatTitle\"    , " +
                        "           average_score \"averageScore\"    , " +
                        "           status_catalog_id \"statusCatalogId\" , " +
                        "           evaluation_period_id \"evaluationPeriodId\"  , " +
                        "           avg_behavioral \"avgBehavioral\"   , " +
                        "           avg_development \"avgDevelopment\"  , " +
                        "           avg_operational \"avgOperational\"  , " +
                        "           weight_behavioral \"weightBehavioral\"    , " +
                        "           weight_development \"weightDevelopment\"   , " +
                        "           weight_operational \"weightOperational\"   , " +
                        "           countitem \"countItem\"   " +
                        "      FROM  " +
                        "          view_evaluation_general_report  eval" +
                        "      WHERE  " +
                        "          c_assessor_post_code IN (  " +
                        "              SELECT  " +
                        "                  *  " +
                        "              FROM  " +
                        "                  (  " +
                        "                      SELECT  " +
                        "                          post_code  " +
                        "                      FROM  " +
                        "                          view_organization_tree  " +
                        "                      START WITH  " +
                        "                          post_code = (  " +
                        "                              SELECT  " +
                        "                                  post_code  " +
                        "                              FROM  " +
                        "                                  view_organization_tree  " +
                        "                              WHERE  " +
                        "                                  national_code = %s  " +
                        "                          )  " +
                        "                      CONNECT BY  " +
                        "                          PRIOR post_code = post_parent_code  " +
                        "                      ORDER BY  " +
                        "                          level  " +
                        "                  )  " +
                        "          )" +
                        "         %s "
                , SecurityUtil.getNationalCode(), whereClause).replaceAll("\\[|\\]", "");
    }

    private String getGeneralReportDetailQuery(String whereClause, Pageable pageable) {
        if (Objects.nonNull(pageable))
            whereClause = MessageFormat.format("{0}{1}", whereClause, String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize()));
        return String.format(" " +
                        " SELECT " +
                        " distinct " +
                        "    evalItem.evaluation_id \"evalId\" , " +
                        "    merit.c_code \"meritCode\" , " +
                        "    merit.c_title  \"meritTitle\" , " +
                        "    catalog.c_value \"effectiveScore\" , " +
                        "    evalItem.c_description \"description\" , " +
                        "    nvl( kpiType.c_title,kpiType1.c_title) \"kpiTitle\" , " +
                        "    nvl(groupTypeMerit.N_WEIGHT,postMerit.N_WEIGHT) \"meritWeight\" " +
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
                        "    Where 1 = 1  %s "
                , whereClause).replaceAll("\\[|\\]", "");
    }

    private StringBuilder getGeneralReportWhereClause(SearchDTO.SearchRq request) {
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
                    case "evaluationIds" -> whereClause.append(" and ").append("evalItem.evaluation_id").append(" in (").append(criteria.getValue().toString()).append(")");
                }
            });
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }
}

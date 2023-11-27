package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationViewDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationViewService;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.mapper.EvaluationViewMapper;
import com.nicico.evaluation.repository.EvaluationViewRepository;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

import static com.nicico.evaluation.utility.EvaluationConstant.FINALIZED;

@RequiredArgsConstructor
@Slf4j
@Service
public class EvaluationViewService implements IEvaluationViewService {

    private final ModelMapper modelMapper;
    private final EvaluationViewMapper mapper;
    private final EvaluationViewRepository repository;
    private final PageableMapper pageableMapper;
    private final IOrganizationTreeService organizationTreeService;
    private final ICatalogService catalogService;
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_LAST_FINALIZED_EVALUATION')")
    public SearchDTO.SearchRs<EvaluationDTO.Info> searchByParent(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
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
    @PreAuthorize("hasAuthority('R_EVALUATION_COMPREHENSIVE')")
    public SearchDTO.SearchRs<EvaluationViewDTO.Info> searchEvaluationComprehensive(SearchDTO.SearchRq request, int count, int startIndex) {

        String whereClause = String.valueOf(getWhereClause(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);

        String query = getSubAssessorQuery(whereClause, pageable);
        List<Map<String, Object>> resultList = parameterJdbcTemplate.queryForList(query, new LinkedHashMap<>());
        List<EvaluationViewDTO.Info> infoList = modelMapper.map(resultList, new TypeToken<List<EvaluationViewDTO.Info>>() {
        }.getType());

        SearchDTO.SearchRs<EvaluationViewDTO.Info> searchRs = new SearchDTO.SearchRs<>();
        searchRs.setList(new ArrayList<>());
        searchRs.setTotalCount(0L);
        if (!resultList.isEmpty()) {
            String totalQuery = getSubAssessorQuery(whereClause, null);
            Long totalCount = jdbcTemplate.queryForObject(MessageFormat.format(" Select count(*) from ({0})", totalQuery), Long.class);
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
        }
        return searchRs;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_COMPREHENSIVE')")
    public ResponseEntity<byte[]> downloadExcelEvaluationComprehensive(List<FilterDTO> criteria) {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, null, null);
        SearchDTO.SearchRs<EvaluationViewDTO.Info> searchRs = this.searchEvaluationComprehensive(request, Integer.MAX_VALUE, 0);
        if (!searchRs.getList().isEmpty()) {
            byte[] body = BaseService.exportExcelByList(searchRs.getList(), "گزارش ارزیابی جامع", "گزارش ارزیابی جامع");
            ExcelGenerator.ExcelDownload excelDownload = new ExcelGenerator.ExcelDownload(body);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                    .body(excelDownload.getContent());
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_LAST_FINALIZED_EVALUATION')")
    public ExcelGenerator.ExcelDownload downloadExcelByParent(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {

        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, null, null);
        List<String> postCodeList = organizationTreeService.getByParentNationalCode();

        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq assessorPostCodeCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.inSet)
                .setFieldName("assessorPostCode")
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

        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, " آخرین ارزیابی های نهایی شده", "گزارش  آخرین ارزیابی های نهایی شده");
        return new ExcelGenerator.ExcelDownload(body);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, "گزارش لیست ارزیابی", "گزارش لیست ارزیابی");
        return new ExcelGenerator.ExcelDownload(body);
    }

    private StringBuilder getWhereClause(SearchDTO.SearchRq request) {
        StringBuilder whereClause = new StringBuilder();
        if (Objects.nonNull(request.getCriteria()) && !request.getCriteria().getCriteria().isEmpty())
            request.getCriteria().getCriteria().forEach(criteria -> {
                switch (criteria.getFieldName()) {
                    case "assessFullName" -> whereClause.append(" and ").append("eval.c_assess_full_name").append(" like '%").append(criteria.getValue().toString()).append("%'");
                    case "assessPostCode" -> whereClause.append(" and ").append("eval.c_assess_post_code").append(" like '%").append(criteria.getValue()).append("%'");
                    case "assessPostTitle" -> whereClause.append(" and ").append("eval.post_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "assessorFullName" -> whereClause.append(" and ").append("eval.c_assessor_full_name").append(" like '%").append(criteria.getValue()).append("%'");
                    case "assessorPostCode" -> whereClause.append(" and ").append("eval.c_assessor_post_code").append(" like '%").append(criteria.getValue()).append("%'");
                    case "assessorPostTitle" -> whereClause.append(" and ").append("eval.assessor_post_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "evaluationPeriodId" -> whereClause.append(" and ").append("evalPeriod.id").append(" like '%").append(criteria.getValue()).append("%'");
                    case "evaluationPeriodStartDateAssessment" -> whereClause.append(" and ").append("evalPeriod.c_start_date_assessment").append(" like '%").append(criteria.getValue()).append("%'");
                    case "evaluationPeriodSndDateAssessment" -> whereClause.append(" and ").append("evalPeriod.c_end_date_assessment").append(" like '%").append(criteria.getValue()).append("%'");
                    case "averageScore" -> whereClause.append(" and ").append("eval.average_score").append(" like '%").append(criteria.getValue()).append("%'");
                    case "postGradeTitle" -> whereClause.append(" and ").append("eval.post_grade_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "mojtamaTitle" -> whereClause.append(" and ").append("eval.mojtama_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "moavenatTitle" -> whereClause.append(" and ").append("eval.moavenat_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "omoorTitle" -> whereClause.append(" and ").append("eval.omoor_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "ghesmatTitle" -> whereClause.append(" and ").append("eval.ghesmat_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "description" -> whereClause.append(" and ").append("eval.c_description").append(" like '%").append(criteria.getValue()).append("%'");
                    case "statusCatalogId" -> whereClause.append(" and ").append("eval.status_catalog_id").append(" like '%").append(criteria.getValue()).append("%'");
                }
            });
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }

    private String getSubAssessorQuery(String whereClause, Pageable pageable) {
        if (Objects.nonNull(pageable))
            whereClause = MessageFormat.format("{0}{1}", whereClause, String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize()));
        return String.format(" SELECT  " +
                        "                 eval.id      \"id\"  , " +
                        "                 eval.c_assess_full_name     \"assessFullName\"  , " +
                        "                 eval.c_assess_national_code     \"assessNationalCode\"  , " +
                        "                 eval.c_assessor_full_name     \"assessorFullName\"  , " +
                        "                 eval.c_assessor_national_code     \"assessorNationalCode\"  , " +
                        "                 eval.c_assess_post_code    \"assessPostCode\"  , " +
                        "                 eval.c_assess_post_code    \"assessPostCode\"  , " +
                        "                 eval.c_assessor_post_code    \"assessorPostCode\"  , " +
                        "                 eval.post_title    \"assessPostTitle\"  , " +
                        "                 eval.assessor_post_title    \"assessorPostTitle\"  , " +
                        "                 eval.cost_center_code  \"costCenterCode\"  , " +
                        "                 eval.cost_center_title \"costCenterTitle\" , " +
                        "                 eval.mojtama_title \"mojtamaTitle\"    , " +
                        "                 eval.moavenat_title \"moavenatTitle\"   , " +
                        "                 eval.omoor_title \"omoorTitle\"  , " +
                        "                 eval.ghesmat_title \"ghesmatTitle\"    , " +
                        "                 eval.average_score \"averageScore\"    , " +
                        "                 eval.post_grade_title \"postGradeTitle\"    , " +
                        "                 eval.c_description \"description\"    , " +
                        "                 eval.status_catalog_id \"statusCatalogId\" , " +
                        "                 eval.evaluation_period_id \"evaluationPeriodId\"  , " +
                        "                 evalPeriod.c_title  \"evaluationPeriodTitle\" , " +
                        "                 evalPeriod.c_start_date_assessment   \"evaluationPeriodStartDateAssessment\" , " +
                        "                 evalPeriod.c_end_date_assessment  \"evaluationPeriodEndDateAssessment\" ,  " +
                        "                 catalog.id   \"statusCatalog.id\" , " +
                        "                 catalog.c_title   \"statusCatalogTitle\" , " +
                        "                 catalog.c_code   \"statusCatalogCode\"  ," +
                        "                 catalog.id   \"statusCatalogId\"  " +
                        "            FROM  " +
                        "                view_evaluation  eval" +
                        "                join tbl_evaluation_period evalPeriod on evalPeriod.id = eval.evaluation_period_id " +
                        "                join tbl_catalog             catalog ON catalog.id = eval.status_catalog_id " +
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
}
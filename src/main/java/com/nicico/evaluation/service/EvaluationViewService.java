package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationViewService;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.mapper.EvaluationViewMapper;
import com.nicico.evaluation.repository.EvaluationViewRepository;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class EvaluationViewService implements IEvaluationViewService {

    private final EvaluationViewMapper mapper;
    private final EvaluationViewRepository repository;
    private final PageableMapper pageableMapper;
    private final EntityManager entityManager;
    private final IOrganizationTreeService organizationTreeService;
    private final ICatalogService catalogService;

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

        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_COMPREHENSIVE')")
    public SearchDTO.SearchRs<EvaluationDTO.Info> searchEvaluationComprehensive(SearchDTO.SearchRq request, int count, int startIndex) {

        List<EvaluationDTO.Info> infoList = new ArrayList<>();
        String whereClause = String.valueOf(getWhereClause(request));
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        String query = getSubAssessorQuery(whereClause, pageable);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setInfoList(infoList, resultList);
            List totalResultList = entityManager.createNativeQuery(getTotalCountSubAssessorQuery(String.valueOf(whereClause))).getResultList();
            Long totalCount = !totalResultList.isEmpty() ? Long.parseLong(totalResultList.get(0).toString()) : 0;
            SearchDTO.SearchRs<EvaluationDTO.Info> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(infoList);
            searchRs.setTotalCount(totalCount);
            return searchRs;
        } else {
            SearchDTO.SearchRs<EvaluationDTO.Info> searchRs = new SearchDTO.SearchRs<>();
            searchRs.setList(new ArrayList<>());
            searchRs.setTotalCount(0L);
            return searchRs;
        }
    }

    private List<EvaluationDTO.Info> setInfoList(List<EvaluationDTO.Info> infoList, List<?> resultList) {
        if (resultList != null) {
            for (Object evaluation : resultList) {
                Object[] eval = (Object[]) evaluation;
                EvaluationDTO.Info evaluationInfo = new EvaluationDTO.Info();
                evaluationInfo.setId(eval[0] == null ? null : Long.parseLong(eval[0].toString()));
                evaluationInfo.setAssessPostCode(eval[2] == null ? null : (eval[2].toString()));
                evaluationInfo.setAssessFullName(eval[9] == null ? null : (eval[9].toString()));
                evaluationInfo.setAssessPostTitle(eval[28] == null ? null : (eval[28].toString()));
                evaluationInfo.setAssessorFullName(eval[8] == null ? null : (eval[8].toString()));
                evaluationInfo.setAssessorPostCode(eval[4] == null ? null : (eval[4].toString()));
                evaluationInfo.setAssessorPostTitle(eval[30] == null ? null : (eval[30].toString()));
                EvaluationDTO.EvaluationPeriodTupleDTO evaluationPeriod = new EvaluationDTO.EvaluationPeriodTupleDTO();
                evaluationInfo.setEvaluationPeriodId(eval[6] == null ? null : (Long.parseLong(eval[6].toString())));
                evaluationPeriod.setId(eval[6] == null ? null : (Long.parseLong(eval[6].toString())));
                evaluationPeriod.setTitle(eval[31] == null ? null : (eval[31].toString()));
                evaluationPeriod.setStartDateAssessment(eval[32] == null ? null : (eval[32].toString()));
                evaluationPeriod.setEndDateAssessment(eval[33] == null ? null : (eval[33].toString()));
                evaluationInfo.setEvaluationPeriod(evaluationPeriod);
                evaluationInfo.setAverageScore(eval[7] == null ? null : Long.parseLong(eval[7].toString()));
                evaluationInfo.setStatusCatalogId(eval[12] == null ? null : Long.parseLong(eval[12].toString()));
                evaluationInfo.setPostGradeTitle(eval[25] == null ? null : (eval[25].toString()));
                evaluationInfo.setMojtamaTitle(eval[21] == null ? null : (eval[21].toString()));
                evaluationInfo.setMoavenatTitle(eval[20] == null ? null : (eval[20].toString()));
                evaluationInfo.setOmoorTitle(eval[22] == null ? null : (eval[22].toString()));
                evaluationInfo.setGhesmatTitle(eval[18] == null ? null : (eval[18].toString()));
                evaluationInfo.setDescription(eval[11] == null ? null : (eval[11].toString()));
                CatalogDTO.Info statusCatalog = new CatalogDTO.Info();
                statusCatalog.setId(eval[12] == null ? null : (Long.parseLong(eval[12].toString())));
                statusCatalog.setTitle(eval[34] == null ? null : (eval[34].toString()));
                evaluationInfo.setStatusCatalog(statusCatalog);
                infoList.add(evaluationInfo);
            }
        }
        return infoList;
    }

    private String getSubAssessorQuery(String whereClause, Pageable pageable) {
        if (Objects.nonNull(pageable))
            whereClause = MessageFormat.format("{0}{1}", whereClause, String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize()));
        return String.format(" SELECT  " +
                        "                 eval.*, " +
                        "                 evalPeriod.c_title, " +
                        "                 evalPeriod.c_start_date_assessment, " +
                        "                 evalPeriod.c_end_date_assessment,  " +
                        "                 catalog.c_title status_catalog_title " +
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

    private String getTotalCountSubAssessorQuery(String whereClause) {

        return String.format(" SELECT  " +
                "                 count(eval.id) " +
                "            FROM  " +
                "                view_evaluation  eval" +
                "                join tbl_evaluation_period evalPeriod on evalPeriod.id = eval.evaluation_period_id " +
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
                    case "assessorFullName" -> whereClause.append(" and ").append("eval.c_assessor_full_name").append(" like '%").append(criteria.getValue()).append("%'");
                    case "assessorPostCode" -> whereClause.append(" and ").append("eval.c_assessor_post_code").append(" like '%").append(criteria.getValue()).append("%'");
                    case "assessorPostTitle" -> whereClause.append(" and ").append("eval.assessor_post_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "evaluationPeriod.id" -> whereClause.append(" and ").append("evalPeriod.id").append(" like '%").append(criteria.getValue()).append("%'");
                    case "evaluationPeriod.startDateAssessment" -> whereClause.append(" and ").append("evalPeriod.c_start_date_assessment").append(" like '%").append(criteria.getValue()).append("%'");
                    case "evaluationPeriod.endDateAssessment" -> whereClause.append(" and ").append("evalPeriod.c_end_date_assessment").append(" like '%").append(criteria.getValue()).append("%'");
                    case "averageScore" -> whereClause.append(" and ").append("eval.average_score").append(" like '%").append(criteria.getValue()).append("%'");
                    case "postGradeTitle" -> whereClause.append(" and ").append("eval.post_grade_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "mojtamaTitle" -> whereClause.append(" and ").append("eval.mojtama_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "moavenatTitle" -> whereClause.append(" and ").append("eval.moavenat_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "omoorTitle" -> whereClause.append(" and ").append("eval.omoor_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "ghesmatTitle" -> whereClause.append(" and ").append("eval.ghesmat_title").append(" like '%").append(criteria.getValue()).append("%'");
                    case "description" -> whereClause.append(" and ").append("eval.c_description").append(" like '%").append(criteria.getValue()).append("%'");
                    case "statusCatalog.id" -> whereClause.append(" and ").append("eval.status_catalog_id").append(" like '%").append(criteria.getValue()).append("%'");
                }
            });
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_COMPREHENSIVE')")
    public ResponseEntity<byte[]> downloadExcelEvaluationComprehensive(List<FilterDTO> criteria) {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, null, null);
        List<EvaluationDTO.Info> infoList = new ArrayList<>();
        String query = getSubAssessorQuery(String.valueOf(getWhereClause(request)), null);
        List<?> resultList = entityManager.createNativeQuery(query).getResultList();
        if (!resultList.isEmpty()) {
            setInfoList(infoList, resultList);
            List<EvaluationDTO.Excel> excelList = mapper.infoDtoToDtoExcelList(infoList);
            byte[] body = BaseService.exportExcelByList(excelList, "گزارش ارزیابی جامع", "گزارش ارزیابی جامع");
            ExcelGenerator.ExcelDownload excelDownload = new ExcelGenerator.ExcelDownload(body);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                    .body(excelDownload.getContent());
        }
        return null;
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
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست ارزیابی");
        return new ExcelGenerator.ExcelDownload(body);
    }
}

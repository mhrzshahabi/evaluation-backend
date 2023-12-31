package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IMeritComponentAuditService;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.mapper.MeritComponentAuditMapper;
import com.nicico.evaluation.model.MeritComponentAudit;
import com.nicico.evaluation.repository.MeritComponentAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MeritComponentAuditService implements IMeritComponentAuditService {

    @Value("${nicico.oauthBackend}")
    private String oAuthUrl;

    @Autowired
    @Qualifier("oauthToken")
    private RestTemplate restTemplateOAuth;

    private final EntityManager entityManager;
    private final PageableMapper pageableMapper;
    private final ICatalogService catalogService;
    private final MeritComponentAuditMapper mapper;
    private final MeritComponentAuditRepository repository;
    private final ResourceBundleMessageSource messageSource;
    private IMeritComponentService meritComponentService;

    @Autowired
    public void setMeritComponentService(@Lazy IMeritComponentService meritComponentService) {
        this.meritComponentService = meritComponentService;
    }

    @Override
    @Transactional
    public MeritComponentAudit findLastActiveByMeritComponentId(Long meritComponentId) {
        return repository.findLastActiveByMeritComponentId(meritComponentId, catalogService.getByCode("Active-Merit").getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
    }

    @Override
    @Transactional(readOnly = true)
    public MeritComponentAudit getPreviousById(Long meritComponentId) {
        MeritComponentAudit meritComponentAudit = repository.getPreviousById(meritComponentId).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return meritComponentAudit;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNumberOfExpertWorkInWorkSpace() {
        String userName = SecurityUtil.getUsername();
        return repository.getNumberOfExpertWorkInWorkSpace(catalogService.getByCode("Re-Examination-Merit").getId(), userName);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNumberOfExpertWorkInWorkSpaceNotification(String token) {
        String userName;
        String url = oAuthUrl + "/tokens/" + token;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, null);
        try {
            OAuthCurrentUserDTO oAuthCurrentUserDTO = restTemplateOAuth.exchange(url, HttpMethod.GET, entity, OAuthCurrentUserDTO.class).getBody();
            if (oAuthCurrentUserDTO != null) {
                userName = oAuthCurrentUserDTO.getPrincipal().getUsername();
                if (userName != null)
                    return repository.getNumberOfExpertWorkInWorkSpace(catalogService.getByCode("Re-Examination-Merit").getId(), userName);
                else
                    throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "شخص مورد نظر در سیستم OAuth یافت نشد");
            } else
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "شخص مورد نظر در سیستم OAuth یافت نشد");
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.Unauthorized, "خطا در دسترسی به سیستم OAuth");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getExpertWorkInWorkSpace() {
        String userName = SecurityUtil.getUsername();
        return repository.getExpertWorkInWorkSpace(catalogService.getByCode("Re-Examination-Merit").getId(), userName);
    }

    @Override
    @Transactional(readOnly = true)
    public MeritComponentAudit findAllByRevAndMeritComponentId(Long rev, Long meritComponentId) {
        return repository.findAllByRevAndMeritComponentId(rev, meritComponentId).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<MeritComponentDTO.Info> searchLastActiveMeritComponent(int startIndex, int count, SearchRequestDTO search) {
        List<MeritComponentDTO.Info> data = new ArrayList<>();
        SearchDTO.SearchRs<MeritComponentDTO.Info> searchRs = new SearchDTO.SearchRs<>();
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Long activeStatus = catalogService.getByCode("Active-Merit").getId();
        Long revokedStatus = catalogService.getByCode("Revoked-Merit").getId();

        try {
            String searchQuery = "";
            if (search != null && search.getSearchDataDTOList() != null && search.getSearchDataDTOList().size() > 0) {
                searchQuery = searchQuery(search.getSearchDataDTOList());
            }
            String query = getMeritComponentsLastActive(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize(),
                    activeStatus, revokedStatus, searchQuery);
            List<?> queryData = entityManager.createNativeQuery(query).getResultList();

            Long totalNumbers = Long.valueOf(entityManager.createNativeQuery(getMeritComponentsLastActiveCount(activeStatus, revokedStatus, searchQuery)).
                    getSingleResult().toString());

            if (queryData != null) {
                for (Object meritComponentAudit : queryData) {
                    Object[] merit = (Object[]) meritComponentAudit;
                    MeritComponentDTO.Info meritComponentInfo = new MeritComponentDTO.Info();
                    meritComponentInfo.setId(merit[0] == null ? null : Long.parseLong(merit[0].toString()));
                    meritComponentInfo.setTitle(merit[6] == null ? null : merit[6].toString());
                    meritComponentInfo.setCode(merit[7] == null ? null : merit[7].toString());
                    meritComponentInfo.setStatusCatalogId(merit[8] == null ? null : Long.parseLong(merit[8].toString()));
                    meritComponentInfo.setDescription(merit[9] == null ? null : merit[9].toString());
                    meritComponentInfo.setMeritComponentTypes(getKpiTypeInfoByMeritComponentId(meritComponentInfo.getId()));
                    meritComponentInfo.setStatusCatalog(merit[8] == null ? null : catalogService.get(Long.parseLong(merit[8].toString())));
                    data.add(meritComponentInfo);
                }
            }
            searchRs.setList(data);
            searchRs.setTotalCount(totalNumbers);
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, null,
                    messageSource.getMessage("exception.bad.data", null, LocaleContextHolder.getLocale()));
        }
        return searchRs;
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<MeritComponentDTO.Info> searchLastActiveMeritComponentKPIFilter(int startIndex, int count, SearchRequestDTO search) {
        List<MeritComponentDTO.Info> data = new ArrayList<>();
        SearchDTO.SearchRs<MeritComponentDTO.Info> searchRs = new SearchDTO.SearchRs<>();
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Long activeStatus = catalogService.getByCode("Active-Merit").getId();
        Long revokedStatus = catalogService.getByCode("Revoked-Merit").getId();

        try {
            String searchQuery = "";
            if (search != null && search.getSearchDataDTOList() != null && search.getSearchDataDTOList().size() > 0) {
                searchQuery = searchQuery(search.getSearchDataDTOList());
            }
            String query = getMeritComponentsLastActiveKPIFilter(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize(),
                    activeStatus, revokedStatus, searchQuery);
            List<?> queryData = entityManager.createNativeQuery(query).getResultList();

            Long totalNumbers = Long.valueOf(entityManager.createNativeQuery(getMeritComponentsLastActiveKPIFilterCount(activeStatus, revokedStatus, searchQuery)).
                    getSingleResult().toString());

            if (queryData != null) {
                for (Object meritComponentAudit : queryData) {
                    Object[] merit = (Object[]) meritComponentAudit;
                    MeritComponentDTO.Info meritComponentInfo = new MeritComponentDTO.Info();
                    meritComponentInfo.setId(merit[0] == null ? null : Long.parseLong(merit[0].toString()));
                    meritComponentInfo.setTitle(merit[6] == null ? null : merit[6].toString());
                    meritComponentInfo.setCode(merit[7] == null ? null : merit[7].toString());
                    meritComponentInfo.setStatusCatalogId(merit[8] == null ? null : Long.parseLong(merit[8].toString()));
                    meritComponentInfo.setDescription(merit[9] == null ? null : merit[9].toString());
                    meritComponentInfo.setMeritComponentTypes(getKpiTypeInfoByMeritComponentId(meritComponentInfo.getId()));
                    meritComponentInfo.setStatusCatalog(merit[8] == null ? null : catalogService.get(Long.parseLong(merit[8].toString())));
                    data.add(meritComponentInfo);
                }
            }
            searchRs.setList(data);
            searchRs.setTotalCount(totalNumbers);
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, null,
                    messageSource.getMessage("exception.bad.data", null, LocaleContextHolder.getLocale()));
        }
        return searchRs;
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<MeritComponentAuditDTO.Info> getChangeList(SearchDTO.SearchRq request, Long id) throws NoSuchFieldException, IllegalAccessException {

        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq idCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.equals)
                .setFieldName("id")
                .setValue(id);

        criteriaRqList.add(idCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    private String searchQuery(List<SearchRequestDTO.SearchDataDTO> searchDataDTOList) {
        StringBuilder query = new StringBuilder();
        if (searchDataDTOList.size() > 0) {
            searchDataDTOList.forEach(search -> {
                        switch (search.getFieldName()) {
                            case "title" -> query.append(" and ").append("c_title").append(" like '%").append(search.getValue().toString()).append("%'");
                            case "code" -> query.append(" and ").append("c_code").append(" like '%").append(search.getValue().toString()).append("%'");
                            case "statusCatalogId" -> query.append(" and ").append("status_catalog_id").append(" like '%").append(search.getValue().toString()).append("%'");
                            case "kpiType.id" -> query.append(" and ").append("kpi_type_id").append(" = ").append(search.getValue().toString());
                            case "meritComponentTypes.kpiType.levelDefCatalog.code" -> query.append(" and ").append("level_def_code").append(" like '%").append(search.getValue().toString()).append("%'");
                            default -> {
                            }
                        }
                    }
            );
            return query.toString();
        } else
            return null;
    }

    private String getMeritComponentsLastActive(int pageNumber, int pageSize, Long activeStatus, Long revokedStatus, String searchQuery) {
        return String.format(
                """
                        SELECT
                            *
                        FROM
                            (
                               SELECT
                                   mcaud.id,
                                   mcaud.rev,
                                   mcaud.c_created_by,
                                   mcaud.d_created_date,
                                   mcaud.c_last_modified_by,
                                   mcaud.d_last_modified_date,
                                   mcaud.c_title,
                                   mcaud.c_code,
                                   mcaud.status_catalog_id,
                                   mcaud.c_description
                               FROM
                                   (
                                       SELECT
                                           id,
                                           rev,
                                           c_created_by,
                                           d_created_date,
                                           c_last_modified_by,
                                           d_last_modified_date,
                                           c_title,
                                           c_code,
                                           status_catalog_id,
                                           c_description,
                                           RANK()
                                           OVER(PARTITION BY id
                                                ORDER BY
                                                    rev DESC
                                           ) rev_rank
                                       FROM
                                           tbl_merit_component_aud
                                       WHERE
                                           tbl_merit_component_aud.status_catalog_id = %s
                                   ) mcaud
                                   INNER JOIN tbl_merit_component ON tbl_merit_component.id = mcaud.id
                               WHERE
                                   rev_rank = 1 AND tbl_merit_component.status_catalog_id <> %s
                            )
                        WHERE
                            1 = 1
                            %s
                        OFFSET %s ROWS FETCH NEXT %s ROWS ONLY
                        """,
                activeStatus,
                revokedStatus,
                searchQuery,
                pageNumber,
                pageSize
        );
    }

    private String getMeritComponentsLastActiveCount(Long activeStatus, Long revokedStatus, String searchQuery) {
        return String.format(
                """
                        SELECT
                            COUNT(id)
                        FROM
                            (
                               SELECT
                                   mcaud.id as id,
                                   mcaud.rev,
                                   mcaud.c_created_by,
                                   mcaud.d_created_date,
                                   mcaud.c_last_modified_by,
                                   mcaud.d_last_modified_date,
                                   mcaud.c_title,
                                   mcaud.c_code,
                                   mcaud.status_catalog_id,
                                   mcaud.c_description
                               FROM
                                   (
                                       SELECT
                                           id,
                                           rev,
                                           c_created_by,
                                           d_created_date,
                                           c_last_modified_by,
                                           d_last_modified_date,
                                           c_title,
                                           c_code,
                                           status_catalog_id,
                                           c_description,
                                           RANK()
                                           OVER(PARTITION BY id
                                                ORDER BY
                                                    rev DESC
                                           ) rev_rank
                                       FROM
                                           tbl_merit_component_aud
                                       WHERE
                                           tbl_merit_component_aud.status_catalog_id = %s
                                   ) mcaud
                                   INNER JOIN tbl_merit_component ON tbl_merit_component.id = mcaud.id
                               WHERE
                                   rev_rank = 1 AND tbl_merit_component.status_catalog_id <> %s
                            )
                        WHERE
                            1 = 1
                            %s
                        """,
                activeStatus,
                revokedStatus,
                searchQuery
        );
    }

    private String getMeritComponentsLastActiveKPIFilter(int pageNumber, int pageSize, Long activeStatus, Long revokedStatus, String searchQuery) {
        return String.format(
                """
                        SELECT
                            *
                        FROM
                            (
                               SELECT
                                   mcaud.id,
                                   mcaud.rev,
                                   mcaud.c_created_by,
                                   mcaud.d_created_date,
                                   mcaud.c_last_modified_by,
                                   mcaud.d_last_modified_date,
                                   mcaud.c_title,
                                   mcaud.c_code,
                                   mcaud.status_catalog_id,
                                   mcaud.c_description,
                                   kpitype.id AS kpi_type_id,
                                   catalog.c_code AS level_def_code
                               FROM
                                   (
                                       SELECT
                                           id,
                                           rev,
                                           c_created_by,
                                           d_created_date,
                                           c_last_modified_by,
                                           d_last_modified_date,
                                           c_title,
                                           c_code,
                                           status_catalog_id,
                                           c_description,
                                           RANK()
                                           OVER(PARTITION BY id
                                                ORDER BY
                                                    rev DESC
                                           ) rev_rank
                                       FROM
                                           tbl_merit_component_aud
                                       WHERE
                                           tbl_merit_component_aud.status_catalog_id = %s
                                   ) mcaud
                                   INNER JOIN tbl_merit_component ON tbl_merit_component.id = mcaud.id
                                   LEFT JOIN tbl_merit_component_type mct ON mct.merit_component_id = mcaud.id
                                   LEFT JOIN tbl_kpi_type             kpitype ON kpitype.id = mct.kpi_type_id
                                   LEFT JOIN tbl_catalog catalog ON catalog.id = kpitype.level_def_id
                               WHERE
                                   rev_rank = 1 AND tbl_merit_component.status_catalog_id <> %s
                            )
                        WHERE
                            1 = 1
                            %s
                        OFFSET %s ROWS FETCH NEXT %s ROWS ONLY
                        """,
                activeStatus,
                revokedStatus,
                searchQuery,
                pageNumber,
                pageSize
        );
    }

    private String getMeritComponentsLastActiveKPIFilterCount(Long activeStatus, Long revokedStatus, String searchQuery) {
        return String.format(
                """
                        SELECT
                            COUNT(id)
                        FROM
                            (
                               SELECT
                                   mcaud.id as id,
                                   mcaud.rev,
                                   mcaud.c_created_by,
                                   mcaud.d_created_date,
                                   mcaud.c_last_modified_by,
                                   mcaud.d_last_modified_date,
                                   mcaud.c_title,
                                   mcaud.c_code,
                                   mcaud.status_catalog_id,
                                   mcaud.c_description,
                                   kpitype.id AS kpi_type_id,
                                   catalog.c_code AS level_def_code
                               FROM
                                   (
                                       SELECT
                                           id,
                                           rev,
                                           c_created_by,
                                           d_created_date,
                                           c_last_modified_by,
                                           d_last_modified_date,
                                           c_title,
                                           c_code,
                                           status_catalog_id,
                                           c_description,
                                           RANK()
                                           OVER(PARTITION BY id
                                                ORDER BY
                                                    rev DESC
                                           ) rev_rank
                                       FROM
                                           tbl_merit_component_aud
                                       WHERE
                                           tbl_merit_component_aud.status_catalog_id = %s
                                   ) mcaud
                                   INNER JOIN tbl_merit_component ON tbl_merit_component.id = mcaud.id
                                   LEFT JOIN tbl_merit_component_type mct ON mct.merit_component_id = mcaud.id
                                   LEFT JOIN tbl_kpi_type             kpitype ON kpitype.id = mct.kpi_type_id
                                   LEFT JOIN tbl_catalog catalog ON catalog.id = kpitype.level_def_id
                               WHERE
                                   rev_rank = 1 AND tbl_merit_component.status_catalog_id <> %s
                            )
                        WHERE
                            1 = 1
                            %s
                        """,
                activeStatus,
                revokedStatus,
                searchQuery
        );
    }

    private MeritComponentTypeDTO.Info getKpiTypeInfoByMeritComponentId(Long meritComponentId) {
        return meritComponentService.get(meritComponentId).getMeritComponentTypes();
    }
}

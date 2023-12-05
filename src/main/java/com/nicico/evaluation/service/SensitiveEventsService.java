package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ISensitiveEventsService;
import com.nicico.evaluation.mapper.SensitiveEventsMapper;
import com.nicico.evaluation.model.SensitiveEvents;
import com.nicico.evaluation.repository.SensitiveEventsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SensitiveEventsService implements ISensitiveEventsService {

    private final ModelMapper modelMapper;
    private final SensitiveEventsMapper mapper;
    private final PageableMapper pageableMapper;
    private final SensitiveEventsRepository repository;
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS')")
    public SensitiveEventsDTO.Info get(Long id) {
        SensitiveEvents sensitiveEvents = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(sensitiveEvents);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS')")
    public List<SensitiveEventsDTO.Info> getAllByNationalCode(String nationalCode) {
        List<SensitiveEvents> allByNationalCode = repository.findAllByNationalCode(nationalCode);
        return mapper.entityToDtoInfoList(allByNationalCode);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS')")
    public SensitiveEventsDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<SensitiveEvents> sensitiveEvents = repository.findAll(pageable);
        List<SensitiveEventsDTO.Info> sensitiveEventsInfos = mapper.entityToDtoInfoList(sensitiveEvents.getContent());
        List<SensitiveEventsDTO.Info> infos = sensitiveEventsInfos.stream().filter(sensitiveEvent -> sensitiveEvent.getNationalCode().equals(SecurityUtil.getNationalCode())).toList();
        SensitiveEventsDTO.Response response = new SensitiveEventsDTO.Response();
        SensitiveEventsDTO.SpecResponse specResponse = new SensitiveEventsDTO.SpecResponse();

        if (Objects.nonNull(infos)) {
            response.setData(infos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) sensitiveEvents.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_SENSITIVE_EVENTS')")
    public SensitiveEventsDTO.Info create(SensitiveEventsDTO.Create dto) {
        SensitiveEvents sensitiveEvents = mapper.dtoCreateToEntity(dto);
        try {
            sensitiveEvents.setNationalCode(SecurityUtil.getNationalCode());
            SensitiveEvents save = repository.save(sensitiveEvents);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_SENSITIVE_EVENTS')")
    public SensitiveEventsDTO.Info update(Long id, SensitiveEventsDTO.Update dto) {
        SensitiveEvents sensitiveEvents = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(sensitiveEvents, dto);
        try {
            SensitiveEvents save = repository.save(sensitiveEvents);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_SENSITIVE_EVENTS')")
    public void delete(Long id) {
        SensitiveEvents sensitiveEvents = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(sensitiveEvents);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS')")
    public SearchDTO.SearchRs<SensitiveEventsDTO.Info> createBySearch(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq nationalCodeCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.equals)
                .setFieldName("nationalCode")
                .setValue(SecurityUtil.getNationalCode());

        criteriaRqList.add(nationalCodeCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS')")
    public SearchDTO.SearchRs<SensitiveEventsDTO.Info> search(SearchDTO.SearchRq request) throws NoSuchFieldException, IllegalAccessException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensitiveEventsDTO.LevelEffectData> getLevelEffectDataForEvaluation(String nationalCode, String startDate, String endDate) {
        String whereClause = String.valueOf(getDateWhereClause(startDate, endDate));
        String query = getLevelEffectDataForEvaluationQuery(nationalCode, whereClause);
        List<Map<String, Object>> resultList = parameterJdbcTemplate.queryForList(query, new LinkedHashMap<>());
        return modelMapper.map(resultList, new TypeToken<List<SensitiveEventsDTO.LevelEffectData>>() {
        }.getType());
    }

    private StringBuilder getDateWhereClause(String startDate, String endDate) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(" AND ").append("se.d_event_date BETWEEN ").append("'").append(startDate).append("' AND '").append(endDate).append("'");
        return new StringBuilder(String.valueOf(whereClause).replaceAll("\\[|\\]", ""));
    }

    private String getLevelEffectDataForEvaluationQuery(String nationalCode, String whereClause) {
        return String.format("SELECT\n" +
                        "    se.n_level_effect   \"levelEffect\",\n" +
                        "    se.d_event_date     \"eventDate\",\n" +
                        "    sep.c_national_code \"nationalCode\",\n" +
                        "    mc.c_code           \"meritComponentCode\",\n" +
                        "    mc.c_title          \"meritComponentTitle\",\n" +
                        "    tc.c_code           \"typeCode\"\n" +
                        "FROM\n" +
                        "    tbl_sensitive_events se\n" +
                        "    JOIN tbl_sensitive_event_person sep ON sep.sensitive_event_id = se.id\n" +
                        "    JOIN tbl_catalog                tc ON tc.id = se.type_catalog_id\n" +
                        "    JOIN tbl_merit_component        mc ON mc.id = sep.merit_component_id\n" +
                        "WHERE\n" +
                        "    sep.c_national_code = %s\n" +
                        "    %s"
                , nationalCode, whereClause);
    }

}

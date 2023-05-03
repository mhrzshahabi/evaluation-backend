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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SensitiveEventsService implements ISensitiveEventsService {

    private final SensitiveEventsMapper mapper;
    private final PageableMapper pageableMapper;
    private final SensitiveEventsRepository repository;

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
    public SearchDTO.SearchRs<SensitiveEventsDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
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

}

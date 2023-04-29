package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.SensitiveEventPersonDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ISensitiveEventPersonService;
import com.nicico.evaluation.mapper.SensitiveEventPersonMapper;
import com.nicico.evaluation.model.SensitiveEventPerson;
import com.nicico.evaluation.repository.SensitiveEventPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SensitiveEventPersonService implements ISensitiveEventPersonService {

    private final SensitiveEventPersonMapper mapper;
    private final PageableMapper pageableMapper;
    private final SensitiveEventPersonRepository repository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS_PERSON')")
    public SensitiveEventPersonDTO.Info get(Long id) {
        SensitiveEventPerson sensitiveEventPerson = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(sensitiveEventPerson);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS_PERSON')")
    public SensitiveEventPersonDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<SensitiveEventPerson> sensitiveEventPersonnel = repository.findAll(pageable);
        List<SensitiveEventPersonDTO.Info> sensitiveEventPersonnelInfos = mapper.entityToDtoInfoList(sensitiveEventPersonnel.getContent());

        SensitiveEventPersonDTO.Response response = new SensitiveEventPersonDTO.Response();
        SensitiveEventPersonDTO.SpecResponse specResponse = new SensitiveEventPersonDTO.SpecResponse();

        if (sensitiveEventPersonnelInfos != null) {
            response.setData(sensitiveEventPersonnelInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) sensitiveEventPersonnel.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_SENSITIVE_EVENTS_PERSON')")
    public SensitiveEventPersonDTO.Info create(SensitiveEventPersonDTO.Create dto) {
        SensitiveEventPerson sensitiveEventPerson = mapper.dtoCreateToEntity(dto);
        try {
            SensitiveEventPerson save = repository.save(sensitiveEventPerson);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_SENSITIVE_EVENTS_PERSON')")
    public SensitiveEventPersonDTO.Info update(SensitiveEventPersonDTO.Update dto) {
        SensitiveEventPerson sensitiveEventPerson = repository.findById(dto.getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(sensitiveEventPerson, dto);
        try {
            SensitiveEventPerson save = repository.save(sensitiveEventPerson);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_SENSITIVE_EVENTS_PERSON')")
    public void delete(Long id) {
        SensitiveEventPerson sensitiveEventPerson = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(sensitiveEventPerson);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS_PERSON')")
    public SearchDTO.SearchRs<SensitiveEventPersonDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

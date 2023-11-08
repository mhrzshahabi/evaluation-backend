package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.iservice.ISensitiveEventPersonViewService;
import com.nicico.evaluation.mapper.SensitiveEventPersonViewMapper;
import com.nicico.evaluation.repository.SensitiveEventPersonViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class SensitiveEventPersonViewService implements ISensitiveEventPersonViewService {

    private final SensitiveEventPersonViewMapper mapper;
    private final SensitiveEventPersonViewRepository repository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SENSITIVE_EVENTS')")
    public SearchDTO.SearchRs<SensitiveEventsDTO.SensitiveEventPersonInfo> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }
}

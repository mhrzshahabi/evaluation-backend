package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CostCenterDTO;
import com.nicico.evaluation.iservice.ICostCenterService;
import com.nicico.evaluation.mapper.CostCenterMapper;
import com.nicico.evaluation.repository.CostCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CostCenterService implements ICostCenterService {

    private final CostCenterMapper mapper;
    private final CostCenterRepository repository;

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<CostCenterDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }
}

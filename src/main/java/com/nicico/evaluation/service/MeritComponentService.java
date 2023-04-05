package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.mapper.MeritComponentMapper;
import com.nicico.evaluation.mapper.MeritComponentMapper;
import com.nicico.evaluation.model.MeritComponent;
import com.nicico.evaluation.repository.MeritComponentRepository;
import com.nicico.evaluation.repository.MeritComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional
public class MeritComponentService implements IMeritComponentService {

    private final MeritComponentRepository repository;
    private final MeritComponentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")

    public MeritComponentDTO.Info get(Long id) throws Exception {
        MeritComponent meritComponent = repository.findById(id).orElseThrow(() -> new Exception("exception.record.not.found"));
        return mapper.entityToDtoInfo(meritComponent);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public List<MeritComponentDTO.Info> list() {
        List<MeritComponent> meritComponents = repository.findAll();
        return mapper.entityToDtoInfoList(meritComponents);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public TotalResponse<MeritComponentDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(repository, request, mapper::entityToDtoInfo);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_MERIT_COMPONENT')")
    public MeritComponentDTO.Info create(MeritComponentDTO.Create dto) {
        MeritComponent meritComponent = mapper.dtoCreateToEntity(dto);
        MeritComponent save = repository.save(meritComponent);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_MERIT_COMPONENT')")
    public MeritComponentDTO.Info update(MeritComponentDTO.Update dto) {
        MeritComponent meritComponent = repository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("MeritComponent not  found"));
        mapper.update(meritComponent, dto);
        MeritComponent save = repository.save(meritComponent);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_MERIT_COMPONENT')")
    public void delete(Long id) {
        MeritComponent meritComponent = repository.findById(id).orElseThrow(() -> new RuntimeException("MeritComponent not  found"));
        repository.delete(meritComponent);
    }

}

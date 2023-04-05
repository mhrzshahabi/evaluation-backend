package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.KPITypeDTO;
import com.nicico.evaluation.repository.KPITypeRepository;
import com.nicico.evaluation.mapper.KPITypeMapper;
import com.nicico.evaluation.model.KPIType;
import com.nicico.evaluation.iservice.IKPITypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional
public class KPITypeService implements IKPITypeService {

    private final KPITypeRepository repository;
    private final KPITypeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_KPI_TYPE')")
    public KPITypeDTO.Info get(Long id) throws Exception {
        KPIType kpiType = repository.findById(id).orElseThrow(() -> new Exception("exception.main-desktop.record.not.found"));
        return mapper.entityToDtoInfo(kpiType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_KPI_TYPE')")
    public List<KPITypeDTO.Info> list() {
        List<KPIType> kpiTypes = repository.findAll();
        return mapper.entityToDtoInfoList(kpiTypes);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_KPI_TYPE')")
    public TotalResponse<KPITypeDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(repository, request, mapper::entityToDtoInfo);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_KPI_TYPE')")
    public KPITypeDTO.Info create(KPITypeDTO.Create dto) {
        KPIType kpiType = mapper.dtoCreateToEntity(dto);
        KPIType save = repository.save(kpiType);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_KPI_TYPE')")
    public KPITypeDTO.Info update(KPITypeDTO.Update dto) {
        KPIType kPIType = repository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("KPITType not  found"));
        mapper.update(kPIType, dto);
        KPIType save = repository.save(kPIType);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_KPI_TYPE')")
    public void delete(Long id) {
        KPIType kPIType = repository.findById(id).orElseThrow(() -> new RuntimeException("KPITType not  found"));
        repository.delete(kPIType);
    }

}

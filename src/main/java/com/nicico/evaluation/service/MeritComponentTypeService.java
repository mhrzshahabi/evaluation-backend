package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IMeritComponentTypeService;
import com.nicico.evaluation.mapper.MeritComponentTypeMapper;
import com.nicico.evaluation.model.MeritComponentType;
import com.nicico.evaluation.repository.MeritComponentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;


@RequiredArgsConstructor
@Service
public class MeritComponentTypeService implements IMeritComponentTypeService {

    private final MeritComponentTypeRepository repository;
    private final MeritComponentTypeMapper mapper;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT_TYPE')")
    public MeritComponentTypeDTO.Info get(Long id) {
        MeritComponentType meritComponentType = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return mapper.entityToDtoInfo(meritComponentType);
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT_TYPE')")
    public List<MeritComponentTypeDTO.Info> list() {
        List<MeritComponentType> meritComponentTypes = repository.findAll();
        return mapper.entityToDtoInfoList(meritComponentTypes);
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT_TYPE')")
    public TotalResponse<MeritComponentTypeDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(repository, request, mapper::entityToDtoInfo);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('C_MERIT_COMPONENT_TYPE')")
    public MeritComponentTypeDTO.Info create(MeritComponentTypeDTO.Create dto) {
        MeritComponentType meritComponentType = mapper.dtoCreateToEntity(dto);
        MeritComponentType save = repository.save(meritComponentType);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('U_MERIT_COMPONENT_TYPE')")
    public MeritComponentTypeDTO.Info update(MeritComponentTypeDTO.Update dto) {
        MeritComponentType meritComponentType = repository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        mapper.update(meritComponentType, dto);
        MeritComponentType save = repository.save(meritComponentType);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('D_MERIT_COMPONENT_TYPE')")
    public void delete(Long id) {
        MeritComponentType meritComponentType = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.delete(meritComponentType);
    }

}

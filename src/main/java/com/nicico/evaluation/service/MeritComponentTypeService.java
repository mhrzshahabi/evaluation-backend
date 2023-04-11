package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IMeritComponentTypeService;
import com.nicico.evaluation.mapper.MeritComponentTypeMapper;
import com.nicico.evaluation.model.MeritComponentType;
import com.nicico.evaluation.repository.MeritComponentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;
import static com.nicico.evaluation.exception.CoreException.NOT_SAVE;


@RequiredArgsConstructor
@Service
public class MeritComponentTypeService implements IMeritComponentTypeService {

    private final PageableMapper pageableMapper;
    private final MeritComponentTypeMapper mapper;
    private final MeritComponentTypeRepository repository;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT_TYPE')")
    public MeritComponentTypeDTO.Info get(Long id) {
        MeritComponentType meritComponentType = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return mapper.entityToDtoInfo(meritComponentType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT_TYPE')")
    public MeritComponentTypeDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<MeritComponentType> meritComponentTypes = repository.findAll(pageable);
        List<MeritComponentTypeDTO.Info> meritComponentTypeInfos = mapper.entityToDtoInfoList(meritComponentTypes.getContent());

        MeritComponentTypeDTO.Response response = new MeritComponentTypeDTO.Response();
        MeritComponentTypeDTO.SpecResponse specResponse = new MeritComponentTypeDTO.SpecResponse();

        if (meritComponentTypeInfos != null) {
            response.setData(meritComponentTypeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) meritComponentTypes.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_MERIT_COMPONENT_TYPE')")
    public MeritComponentTypeDTO.Info create(MeritComponentTypeDTO.Create dto) {
        MeritComponentType meritComponentType = mapper.dtoCreateToEntity(dto);
        try {
            MeritComponentType save = repository.save(meritComponentType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_MERIT_COMPONENT_TYPE')")
    public MeritComponentTypeDTO.Info update(MeritComponentTypeDTO.Update dto) {
        MeritComponentType meritComponentType = repository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        mapper.update(meritComponentType, dto);
        try {
            MeritComponentType save = repository.save(meritComponentType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_MERIT_COMPONENT_TYPE')")
    public void delete(Long id) {
        MeritComponentType meritComponentType = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.delete(meritComponentType);
    }


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT_TYPE')")
    public SearchDTO.SearchRs<MeritComponentTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);

    }
}

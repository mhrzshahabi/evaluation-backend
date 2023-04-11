package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.mapper.MeritComponentMapper;
import com.nicico.evaluation.model.MeritComponent;
import com.nicico.evaluation.repository.MeritComponentRepository;
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
public class MeritComponentService implements IMeritComponentService {

    private final MeritComponentMapper mapper;
    private final PageableMapper pageableMapper;
    private final MeritComponentRepository repository;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public MeritComponentDTO.Info get(Long id) {
        MeritComponent meritComponent = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return mapper.entityToDtoInfo(meritComponent);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public MeritComponentDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<MeritComponent> meritComponents = repository.findAll(pageable);
        List<MeritComponentDTO.Info> meritComponentInfos = mapper.entityToDtoInfoList(meritComponents.getContent());

        MeritComponentDTO.Response response = new MeritComponentDTO.Response();
        MeritComponentDTO.SpecResponse specResponse = new MeritComponentDTO.SpecResponse();

        if (meritComponentInfos != null) {
            response.setData(meritComponentInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) meritComponents.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_MERIT_COMPONENT')")
    public MeritComponentDTO.Info create(MeritComponentDTO.Create dto) {
        MeritComponent meritComponent = mapper.dtoCreateToEntity(dto);
        try {
            MeritComponent save = repository.save(meritComponent);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_MERIT_COMPONENT')")
    public MeritComponentDTO.Info update(MeritComponentDTO.Update dto) {
        MeritComponent meritComponent = repository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        mapper.update(meritComponent, dto);
        try {
            MeritComponent save = repository.save(meritComponent);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_MERIT_COMPONENT')")
    public void delete(Long id) {
        MeritComponent meritComponent = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.delete(meritComponent);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public SearchDTO.SearchRs<MeritComponentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);

    }

}

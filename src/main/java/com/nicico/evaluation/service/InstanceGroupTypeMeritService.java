package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IInstanceGroupTypeMeritService;
import com.nicico.evaluation.mapper.InstanceGroupTypeMeritMapper;
import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import com.nicico.evaluation.repository.InstanceGroupTypeMeritRepository;
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
public class InstanceGroupTypeMeritService implements IInstanceGroupTypeMeritService {

    private final InstanceGroupTypeMeritMapper mapper;
    private final PageableMapper pageableMapper;
    private final InstanceGroupTypeMeritRepository repository;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public InstanceGroupTypeMeritDTO.Info get(Long id) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return mapper.entityToDtoInfo(instanceGroupTypeMerit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public InstanceGroupTypeMeritDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<InstanceGroupTypeMerit> instanceGroupTypeMerits = repository.findAll(pageable);
        List<InstanceGroupTypeMeritDTO.Info> groupTypeInfos = mapper.entityToDtoInfoList(instanceGroupTypeMerits.getContent());

        InstanceGroupTypeMeritDTO.Response response = new InstanceGroupTypeMeritDTO.Response();
        InstanceGroupTypeMeritDTO.SpecResponse specResponse = new InstanceGroupTypeMeritDTO.SpecResponse();

        if (groupTypeInfos != null) {
            response.setData(groupTypeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) instanceGroupTypeMerits.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public TotalResponse<InstanceGroupTypeMeritDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(repository, request, mapper::entityToDtoInfo);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_INSTANCE_GROUP_TYPE_MERIT')")
    public InstanceGroupTypeMeritDTO.Info create(InstanceGroupTypeMeritDTO.Create dto) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = mapper.dtoCreateToEntity(dto);
        try {
            InstanceGroupTypeMerit save = repository.save(instanceGroupTypeMerit);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_INSTANCE_GROUP_TYPE_MERIT')")
    public InstanceGroupTypeMeritDTO.Info update(InstanceGroupTypeMeritDTO.Update dto) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        mapper.update(instanceGroupTypeMerit, dto);
        try {
            InstanceGroupTypeMerit save = repository.save(instanceGroupTypeMerit);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_INSTANCE_GROUP_TYPE_MERIT')")
    public void delete(Long id) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.delete(instanceGroupTypeMerit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public SearchDTO.SearchRs<InstanceGroupTypeMeritDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }


}
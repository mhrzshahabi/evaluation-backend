package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.common.PageDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IInstanceService;
import com.nicico.evaluation.mapper.InstanceMapper;
import com.nicico.evaluation.model.Instance;
import com.nicico.evaluation.repository.InstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;


@RequiredArgsConstructor
@Service
public class InstanceService implements IInstanceService {

    private final InstanceRepository instanceRepository;
    private final InstanceMapper instanceMapper;
    private final PageableMapper pageableMapper;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public PageDTO list(Pageable pageable) {
        Page<Instance> instances = instanceRepository.findAll(pageable);
        List<InstanceDTO.Info> instanceDto = instanceMapper.entityToDtoInfoList(instances.getContent());
        return pageableMapper.toPageDto(instances, instanceDto);
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public InstanceDTO.Info get(Long id) {
        Instance instance = instanceRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return instanceMapper.entityToDtoInfo(instance);
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public TotalResponse<InstanceDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(instanceRepository, request, instanceMapper::entityToDtoInfo);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('C_INSTANCE')")
    public InstanceDTO.Info create(InstanceDTO.Create dto) {
        Instance instance = instanceMapper.dtoCreateToEntity(dto);
        instance = instanceRepository.save(instance);
        return instanceMapper.entityToDtoInfo(instance);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('U_INSTANCE')")
    public InstanceDTO.Info update(InstanceDTO.Update dto) {
        Instance instance = instanceRepository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        instanceMapper.update(instance, dto);
        Instance save = instanceRepository.save(instance);
        return instanceMapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('D_INSTANCE')")
    public void delete(Long id) {
        Instance instance = instanceRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        instanceRepository.delete(instance);
    }
}

package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.NotFoundException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IInstanceService;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.mapper.InstanceMapper;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.model.Instance;
import com.nicico.evaluation.repository.GroupRepository;
import com.nicico.evaluation.repository.InstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;


@RequiredArgsConstructor
@Service
@Transactional
public class InstanceService implements IInstanceService {

    private final InstanceRepository instanceRepository;
    private final InstanceMapper instanceMapper;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('')")
    public List<InstanceDTO.Info> list() {
        List<Instance> instances = instanceRepository.findAll();
        return instanceMapper.entityToDtoInfoList(instances);
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('')")
    public InstanceDTO.Info get(Long id) {
        Instance instance = instanceRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        InstanceDTO.Info instanceDtoInfo = instanceMapper.entityToDtoInfo(instance);
        return instanceDtoInfo;
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('')")
    public TotalResponse<InstanceDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(instanceRepository, request, instanceMapper::entityToDtoInfo);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('')")
    public InstanceDTO.Info create(InstanceDTO.Create dto) {
        Instance instance = instanceMapper.dtoCreateToEntity(dto);
        instance = instanceRepository.save(instance);
        return  instanceMapper.entityToDtoInfo(instance);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('')")
    public InstanceDTO.Info update(InstanceDTO.Update dto) {
        Instance instance =  instanceRepository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        instanceMapper.update(instance, dto);
        Instance save = instanceRepository.save(instance);
        return instanceMapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('')")
    public void delete(Long id) {
        Instance instance = instanceRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        instanceRepository.delete(instance);
    }
}

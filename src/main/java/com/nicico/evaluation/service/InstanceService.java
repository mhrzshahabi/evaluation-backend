package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IInstanceService;
import com.nicico.evaluation.mapper.InstanceMapper;
import com.nicico.evaluation.model.Instance;
import com.nicico.evaluation.repository.InstanceRepository;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InstanceService implements IInstanceService {

    private final InstanceMapper mapper;
    private final InstanceRepository repository;
    private final PageableMapper pageableMapper;

    @Override
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportAllExcel(repository, mapper::entityToDtoExcel, criteria, null);
        return new ExcelGenerator.ExcelDownload(body);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public InstanceDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Instance> instances = repository.findAll(pageable);
        List<InstanceDTO.Info> instanceInfos = mapper.entityToDtoInfoList(instances.getContent());

        InstanceDTO.Response response = new InstanceDTO.Response();
        InstanceDTO.SpecResponse specResponse = new InstanceDTO.SpecResponse();

        if (instanceInfos != null) {
            response.setData(instanceInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) instances.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public InstanceDTO.Info get(Long id) {
        Instance instance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(instance);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_INSTANCE')")
    public InstanceDTO.Info create(InstanceDTO.Create dto) {
        Instance instance = mapper.dtoCreateToEntity(dto);
        instance = repository.save(instance);
        return mapper.entityToDtoInfo(instance);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_INSTANCE')")
    public InstanceDTO.Info update(InstanceDTO.Update dto) {
        Instance instance = repository.findById(dto.getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(instance, dto);
        Instance save = repository.save(instance);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_INSTANCE')")
    public void delete(Long id) {
        Instance instance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(instance);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public SearchDTO.SearchRs<InstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

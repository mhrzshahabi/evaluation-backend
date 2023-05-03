package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IInstanceGroupTypeMeritService;
import com.nicico.evaluation.iservice.IInstanceService;
import com.nicico.evaluation.mapper.InstanceGroupTypeMeritMapper;
import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import com.nicico.evaluation.repository.InstanceGroupTypeMeritRepository;
import com.nicico.evaluation.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InstanceGroupTypeMeritService implements IInstanceGroupTypeMeritService {

    private final PageableMapper pageableMapper;
    private final IInstanceService instanceService;
    private final InstanceGroupTypeMeritMapper mapper;
    private final InstanceGroupTypeMeritRepository repository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public InstanceGroupTypeMeritDTO.Info get(Long id) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(instanceGroupTypeMerit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public List<InstanceGroupTypeMeritDTO.InstanceInfo> getAllInstanceByGroupTypeMeritId(Long id) {
        List<InstanceGroupTypeMerit> allByGroupTypeMeritId = repository.getAllByGroupTypeMeritId(id);
        return mapper.entityToDtoInstanceList(allByGroupTypeMeritId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public List<InstanceGroupTypeMeritDTO.Info> getAllByGroupTypeMeritId(Long id) {
        List<InstanceGroupTypeMerit> allByGroupTypeMeritId = repository.getAllByGroupTypeMeritId(id);
        return mapper.entityToDtoInfoList(allByGroupTypeMeritId);
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
    public List<InstanceGroupTypeMeritDTO.Info> createAll(List<InstanceGroupTypeMeritDTO.Create> requests) {
        return requests.stream().map(this::create).toList();
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
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }
    // in progress
    @Override
    @PreAuthorize("hasAuthority('C_INSTANCE_GROUP_TYPE_MERIT')")
    public BaseResponse batchCreate(InstanceGroupTypeMeritDTO.BatchCreate dto) {
        BaseResponse response = new BaseResponse();
//        try {
//            Long instanceId = instanceService.getByCode(dto.getInstanceCode()).getId();
//            PostMeritComponentDTO.BatchCreate batchCreate = mapper.dtoBatchCreateToDtoComponentBatchCreate(dto);
//            BaseResponse postMeritComponentResponse = postMeritComponentService.batchCreate(batchCreate);
//            if (postMeritComponentResponse.getStatus() == HttpStatus.OK.value()) {
//                PostMeritInstanceDTO.Create create = new PostMeritInstanceDTO.Create();
//                create.setPostMeritComponentId(Long.valueOf(postMeritComponentResponse.getMessage()));
//                create.setInstanceId(instanceId);
//                create(create);
//                response.setStatus(HttpStatus.OK.value());
//            } else {
//                response.setStatus(HttpStatus.FORBIDDEN.value());
//                response.setMessage(postMeritComponentResponse.getMessage());
//            }
//        } catch (Exception exception) {
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            response.setMessage(exception.getMessage());
//        }
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_INSTANCE_GROUP_TYPE_MERIT')")
    public InstanceGroupTypeMeritDTO.Info update(Long id, InstanceGroupTypeMeritDTO.Update dto) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(instanceGroupTypeMerit, dto);
        try {
            InstanceGroupTypeMerit save = repository.save(instanceGroupTypeMerit);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_INSTANCE_GROUP_TYPE_MERIT')")
    public void delete(Long id) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(instanceGroupTypeMerit);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_INSTANCE_GROUP_TYPE_MERIT')")
    public void deleteAll(List<Long> ids) {
        ids.forEach(this::delete);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public SearchDTO.SearchRs<InstanceGroupTypeMeritDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

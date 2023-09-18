package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.InstanceGroupTypeMeritMapper;
import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import com.nicico.evaluation.repository.InstanceGroupTypeMeritRepository;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class InstanceGroupTypeMeritService implements IInstanceGroupTypeMeritService {

    private final PageableMapper pageableMapper;
    private final InstanceGroupTypeMeritMapper mapper;
    private final InstanceGroupTypeMeritRepository repository;
    private final ExceptionUtil exceptionUtil;
    private final ResourceBundleMessageSource messageSource;
    private final IGroupTypeService groupTypeService;
    private final IMeritComponentService meritComponentService;
    private final IInstanceService instanceService;
    private IGroupTypeMeritService groupTypeMeritService;

    @Autowired
    public void setGroupTypeMeritService(@Lazy IGroupTypeMeritService groupTypeMeritService) {
        this.groupTypeMeritService = groupTypeMeritService;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE_GROUP_TYPE_MERIT')")
    public InstanceGroupTypeMeritDTO.Info get(Long id) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(id).orElseThrow(() ->
                new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
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
    public List<InstanceGroupTypeMerit> getInstanceByGroupTypeMeritId(Long id) {
        return repository.getAllByGroupTypeMeritId(id);
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

    @Override
    public BaseResponse batchCreate(InstanceGroupTypeMeritDTO.BatchCreate dto) {

        BaseResponse response = new BaseResponse();
        StringBuilder errorMessage = new StringBuilder();
        GroupTypeDTO.Info groupTypeByCode = groupTypeService.getByCode(dto.getGroupTypeCode());
        try {
            if (Objects.isNull(groupTypeByCode)) {
                errorMessage.append(messageSource.getMessage("exception.not.exist.group-type",
                        new Object[]{dto.getGroupTypeCode()}, LocaleContextHolder.getLocale()));
            }
            MeritComponentDTO.Info meritComponentByCode = meritComponentService.getByCode(dto.getMeritComponentCode());
            if (Objects.isNull(meritComponentByCode)) {
                errorMessage.append(response.getMessage() + "  " + messageSource.getMessage("exception.not.exist.merit-component",
                        new Object[]{dto.getGroupTypeCode()}, LocaleContextHolder.getLocale()));
            }
            InstanceDTO.Info instanceByCode = instanceService.getByCode(dto.getInstanceCode());
            if (Objects.isNull(instanceByCode)) {
                errorMessage.append(messageSource.getMessage("exception.not.exist.instance",
                        new Object[]{dto.getGroupTypeCode()}, LocaleContextHolder.getLocale()));
            }

            GroupTypeMeritDTO.Create groupTypeMeritCreateDTO = new GroupTypeMeritDTO.Create();
            groupTypeMeritCreateDTO.setGroupTypeId(groupTypeByCode.getId());
            groupTypeMeritCreateDTO.setMeritComponentId(meritComponentByCode.getId());
            groupTypeMeritCreateDTO.setInstanceIds(Collections.singletonList(instanceByCode.getId()));
            groupTypeMeritCreateDTO.setWeight(dto.getWeight());
            groupTypeMeritService.create(groupTypeMeritCreateDTO);
            response.setStatus(HttpStatus.OK.value());

        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            errorMessage.append(Objects.nonNull(exception.getMessage()) ? exception.getMessage() : "");
            response.setMessage(String.valueOf(errorMessage));
        }
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_INSTANCE_GROUP_TYPE_MERIT')")
    public InstanceGroupTypeMeritDTO.Info update(Long id, InstanceGroupTypeMeritDTO.Update dto) {
        InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(id).orElseThrow(() ->
                new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
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
        try {
            InstanceGroupTypeMerit instanceGroupTypeMerit = repository.findById(id).orElseThrow(() ->
                    new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
            repository.delete(instanceGroupTypeMerit);
        } catch (DataIntegrityViolationException violationException) {
            String msg = exceptionUtil.getRecordsByParentId(violationException, id);
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null,
                    messageSource.getMessage("exception.integrity.constraint", null, LocaleContextHolder.getLocale()) + msg);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_INSTANCE_GROUP_TYPE_MERIT')")
    public void deleteByGroupTypeMerit(Long groupTypeMeritId) {
        repository.deleteByGroupTypeMeritId(groupTypeMeritId);
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
    public SearchDTO.SearchRs<InstanceGroupTypeMeritDTO.Info> search(SearchDTO.SearchRq request)
            throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}



package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGroupTypeMeritService;
import com.nicico.evaluation.iservice.IInstanceGroupTypeMeritService;
import com.nicico.evaluation.mapper.GroupTypeMeritMapper;
import com.nicico.evaluation.model.GroupTypeMerit;
import com.nicico.evaluation.repository.GroupTypeMeritRepository;
import com.nicico.evaluation.utility.ExcelGenerator;
import com.nicico.evaluation.utility.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class GroupTypeMeritService implements IGroupTypeMeritService {

    private final GroupTypeMeritMapper mapper;
    private final PageableMapper pageableMapper;
    private final GroupTypeMeritRepository repository;
    private final IInstanceGroupTypeMeritService instanceGroupTypeMeritService;
    private final ExceptionUtil exceptionUtil;
    private final ResourceBundleMessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE_MERIT')")
    public GroupTypeMeritDTO.Info get(Long id) {
        GroupTypeMerit groupTypeMerit = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(groupTypeMerit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE_MERIT')")
    public List<EvaluationItemDTO.MeritTupleDTO> getAllByGroupType(Long groupTypeId) {
        List<GroupTypeMerit> groupTypeMerit = repository.getAllByGroupTypeId(groupTypeId);
        return mapper.entityToEvaluationItemDtoList(groupTypeMerit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE_MERIT')")
    public List<GroupTypeMeritDTO.Info> getAllByGroupTypeIdAndMeritStatusId(Long groupTypeId, Long statusCatalogId) {
        List<GroupTypeMerit> groupTypeMerit = repository.getAllByGroupTypeIdAndMeritStatusId(groupTypeId,statusCatalogId);
        return mapper.entityToDtoInfoList(groupTypeMerit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE_MERIT')")
    public Long getTotalComponentWeightByGroupType(Long groupTypeId) {
        List<GroupTypeMerit> groupTypeMeritList = repository.getAllByGroupTypeId(groupTypeId);
        return groupTypeMeritList.stream().map(GroupTypeMerit::getWeight).reduce(0L, Long::sum);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE_MERIT')")
    public GroupTypeMeritDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<GroupTypeMerit> groupTypeMerits = repository.findAll(pageable);
        List<GroupTypeMeritDTO.Info> groupTypeInfos = mapper.entityToDtoInfoList(groupTypeMerits.getContent());

        GroupTypeMeritDTO.Response response = new GroupTypeMeritDTO.Response();
        GroupTypeMeritDTO.SpecResponse specResponse = new GroupTypeMeritDTO.SpecResponse();

        if (groupTypeInfos != null) {
            response.setData(groupTypeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) groupTypeMerits.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP_TYPE_MERIT')")
    public GroupTypeMeritDTO.Info create(GroupTypeMeritDTO.Create dto) {
        GroupTypeMerit groupTypeMerit = mapper.dtoCreateToEntity(dto);
        Long totalComponentWeightByGroupType = this.getTotalComponentWeightByGroupType(dto.getGroupTypeId());
        if (totalComponentWeightByGroupType + dto.getWeight() > 100)
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave, null,
                    messageSource.getMessage("exception.group-type.weight.limit", null, LocaleContextHolder.getLocale()));
        try {
            GroupTypeMerit groupTypeMeritAdd = repository.save(groupTypeMerit);
            createAllInstanceGroupTypeMerit(dto.getInstanceIds(), groupTypeMeritAdd.getId());
            return mapper.entityToDtoInfo(groupTypeMeritAdd);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.DuplicateRecord, null, messageSource.getMessage("exception.group.type.merit.duplicate", null, locale));
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP_TYPE_MERIT')")
    public GroupTypeMeritDTO.Info update(Long id, GroupTypeMeritDTO.Update dto) {
        GroupTypeMerit groupTypeMerit = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(groupTypeMerit, dto);

        List<Long> instanceGroupTypeIds = instanceGroupTypeMeritService.getAllByGroupTypeMeritId(id).stream().map(InstanceGroupTypeMeritDTO.Info::getId).toList();
        if (!instanceGroupTypeIds.isEmpty())
            instanceGroupTypeMeritService.deleteAll(instanceGroupTypeIds);
        createAllInstanceGroupTypeMerit(dto.getInstanceIds(), id);
        GroupTypeMerit save = repository.save(groupTypeMerit);
        return mapper.entityToDtoInfo(save);

    }

    private void createAllInstanceGroupTypeMerit(List<Long> instanceIds, Long id) {
        try {
            List<InstanceGroupTypeMeritDTO.Create> instanceGroupTypeMeritDTOList = new ArrayList<>();
            instanceIds.forEach(instanceId -> {
                InstanceGroupTypeMeritDTO.Create instanceGroupTypeMeritDTO = new InstanceGroupTypeMeritDTO.Create();
                instanceGroupTypeMeritDTO.setInstanceId(instanceId);
                instanceGroupTypeMeritDTO.setGroupTypeMeritId(id);
                instanceGroupTypeMeritDTOList.add(instanceGroupTypeMeritDTO);
            });
            instanceGroupTypeMeritService.createAll(instanceGroupTypeMeritDTOList);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP_TYPE_MERIT')")
    public void delete(Long id) {
        GroupTypeMerit groupTypeMerit = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            instanceGroupTypeMeritService.deleteByGroupTypeMerit(groupTypeMerit.getId());
            repository.delete(groupTypeMerit);
        } catch (DataIntegrityViolationException violationException) {
            String msg = exceptionUtil.getRecordsByParentId(violationException, id);
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null,
                    messageSource.getMessage("exception.integrity.constraint", null, LocaleContextHolder.getLocale()) + msg);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE_MERIT')")
    public SearchDTO.SearchRs<GroupTypeMeritDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE_MERIT')")
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست مولفه شایستگی گروه-نوع");
        return new ExcelGenerator.ExcelDownload(body);
    }

}

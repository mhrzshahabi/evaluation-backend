package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGroupTypeService;
import com.nicico.evaluation.iservice.IKPITypeService;
import com.nicico.evaluation.mapper.GroupTypeMapper;
import com.nicico.evaluation.model.GroupType;
import com.nicico.evaluation.repository.GroupTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroupTypeService implements IGroupTypeService {

    private final GroupTypeMapper mapper;
    private final PageableMapper pageableMapper;
    private final GroupTypeRepository repository;
    private final IKPITypeService kpiTypeService;
    private final MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public GroupTypeDTO.Info get(Long id) {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(groupType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public List<GroupType> getTypeByAssessPostCode(String assessPostCode, String levelDef) {
        return repository.getTypeByAssessPostCode(assessPostCode, levelDef);
    }


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public GroupTypeDTO.GroupTypeMaxWeight getWeightInfoByGroupId(Long groupId) {
        int kpiSize = kpiTypeService.findAll().size();
        List<GroupType> allByGroupIdAndKpiTypeId = repository.getAllByGroupId(groupId);
        long totalWeightCreated = allByGroupIdAndKpiTypeId.stream().mapToLong(GroupType::getWeight).sum();
        long totalCountOfCreated = allByGroupIdAndKpiTypeId.size();
        long remainCount = kpiSize - totalCountOfCreated;
        GroupTypeDTO.GroupTypeMaxWeight data = new GroupTypeDTO.GroupTypeMaxWeight();
        data.setMaxWeight(100 - remainCount + 1 - totalWeightCreated);
        data.setRemainCount(remainCount);
        return data;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public GroupTypeDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<GroupType> groupTypes = repository.findAll(pageable);
        List<GroupTypeDTO.Info> groupTypeInfos = mapper.entityToDtoInfoList(groupTypes.getContent());

        GroupTypeDTO.Response response = new GroupTypeDTO.Response();
        GroupTypeDTO.SpecResponse specResponse = new GroupTypeDTO.SpecResponse();

        if (groupTypeInfos != null) {
            response.setData(groupTypeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) groupTypes.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP_TYPE')")
    public GroupTypeDTO.Info create(GroupTypeDTO.Create dto) {
        GroupType groupType = mapper.dtoCreateToEntity(dto);
        GroupType allByGroupIdAndKpiTypeId = getAllByGroupIdAndKpiTypeId(groupType);
        if (Objects.nonNull(allByGroupIdAndKpiTypeId))
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.DuplicateRecord, null, messageSource.getMessage("exception.duplicate.information", null, LocaleContextHolder.getLocale()));
        try {
            GroupType save = repository.save(groupType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public GroupType getAllByGroupIdAndKpiTypeId(GroupType groupType) {
        return repository.getByGroupIdAndKpiTypeId(groupType.getGroupId(), groupType.getKpiTypeId());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP_TYPE')")
    public GroupTypeDTO.Info update(Long id, GroupTypeDTO.Update dto) {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(groupType, dto);
        try {
            GroupType save = repository.save(groupType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP_TYPE')")
    public void delete(Long id) {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(groupType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public SearchDTO.SearchRs<GroupTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        SearchDTO.SearchRs<GroupTypeDTO.Info> infoSearchRs = BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
        List<GroupTypeDTO.Info> info = infoSearchRs.getList().stream().toList();
        int kpiSize = kpiTypeService.findAll().size();
        Map<Long, List<GroupTypeDTO.Info>> groupTypeMap = info.stream().collect(Collectors.groupingBy(GroupTypeDTO::getGroupId));
        List<GroupTypeDTO.Info> data = new ArrayList<>();
        groupTypeMap.forEach((groupId, gType) -> {
            long totalWeight = gType.stream().mapToLong(GroupTypeDTO::getWeight).sum();
            gType.forEach(groupType -> {
                groupType.setTotalWeight(totalWeight);
                groupType.setHasAllKpiType(gType.size() == kpiSize ? Boolean.TRUE : Boolean.FALSE);
                data.add(groupType);
            });
        });
        infoSearchRs.setList(data);
        return infoSearchRs;
    }

}

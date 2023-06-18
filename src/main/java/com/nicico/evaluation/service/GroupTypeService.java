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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupTypeService implements IGroupTypeService {

    private final GroupTypeMapper mapper;
    private final PageableMapper pageableMapper;
    private final GroupTypeRepository repository;
    private final IKPITypeService kpiTypeService;

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
    public List<GroupType> getTypeByEvaluationId(Long evaluationId, String levelDef) {
        return repository.getTypeByEvaluationId(evaluationId, levelDef);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public GroupTypeDTO.GroupTypeMaxMinWeight getWeightInfoByGroupId(Long groupId) {
        int kpiSize = kpiTypeService.findAll().size();
        List<GroupType> allByGroupIdAndKpiTypeId = repository.getAllByGroupId(groupId);
        long totalWeightCreated = allByGroupIdAndKpiTypeId.stream().mapToLong(GroupType::getWeight).sum();
        long totalCountOfCreated = allByGroupIdAndKpiTypeId.size();
        long remainCount = kpiSize - totalCountOfCreated;
        GroupTypeDTO.GroupTypeMaxMinWeight data = new GroupTypeDTO.GroupTypeMaxMinWeight();
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
        try {
            GroupType save = repository.save(groupType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
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
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

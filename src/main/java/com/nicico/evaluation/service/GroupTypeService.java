package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGroupTypeMeritService;
import com.nicico.evaluation.iservice.IGroupTypeService;
import com.nicico.evaluation.mapper.GroupTypeMapper;
import com.nicico.evaluation.model.GroupType;
import com.nicico.evaluation.repository.GroupTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupTypeService implements IGroupTypeService {

    private final GroupTypeMapper mapper;
    private final PageableMapper pageableMapper;
    private final GroupTypeRepository repository;
    private final IGroupTypeMeritService groupTypeMeritService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public GroupTypeDTO.Info get(Long id) {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(groupType);
    }

//    @Override
//    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
//    public List<GroupTypeDTO.Info> getTypeByAssessPostCode(String assessPostCode) {
//        List<GroupType> groupType = repository.getTypeByAssessPostCode(assessPostCode);
//        List<EvaluationItemDTO.CreateItemInfo> createItemInfoList = new ArrayList<>();
//        groupType.forEach(gType -> {
//            EvaluationItemDTO.CreateItemInfo createItemInfo = new EvaluationItemDTO.CreateItemInfo();
//            createItemInfo.setGroupTypeWeight(gType.getWeight());
//            createItemInfo.setTypeTitle(gType.getKpiType().getTitle());
//            List<EvaluationItemDTO.GroupTypeMeritTuple> groupTypeMerits = groupTypeMeritService.getAllByGroupTypeId(gType.getId());
//            createItemInfo.setGroupTypeMerit(groupTypeMerits);
//
//        });
//        return mapper.entityToDtoInfoList(groupType);
//    }

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
    @PreAuthorize("hasAuthority('C_EVALUATION_ITEM')")
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
    @PreAuthorize("hasAuthority('U_EVALUATION_ITEM')")
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
    @PreAuthorize("hasAuthority('D_EVALUATION_ITEM')")
    public void delete(Long id) {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(groupType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public SearchDTO.SearchRs<GroupTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

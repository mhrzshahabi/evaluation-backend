package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGroupTypeMeritService;
import com.nicico.evaluation.mapper.GroupTypeMeritMapper;
import com.nicico.evaluation.model.GroupTypeMerit;
import com.nicico.evaluation.repository.GroupTypeMeritRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupTypeMeritService implements IGroupTypeMeritService {

    private final GroupTypeMeritMapper mapper;
    private final PageableMapper pageableMapper;
    private final GroupTypeMeritRepository repository;

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
        try {
            GroupTypeMerit save = repository.save(groupTypeMerit);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP_TYPE_MERIT')")
    public GroupTypeMeritDTO.Info update(GroupTypeMeritDTO.Update dto) {
        GroupTypeMerit groupTypeMerit = repository.findById(dto.getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(groupTypeMerit, dto);
        try {
            GroupTypeMerit save = repository.save(groupTypeMerit);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP_TYPE_MERIT')")
    public void delete(Long id) {
        GroupTypeMerit groupTypeMerit = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(groupTypeMerit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE_MERIT')")
    public SearchDTO.SearchRs<GroupTypeMeritDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

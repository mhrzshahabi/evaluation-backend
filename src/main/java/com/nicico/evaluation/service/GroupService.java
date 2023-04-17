package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGroupGradeService;
import com.nicico.evaluation.iservice.IGroupService;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupService implements IGroupService {

    private final GroupMapper mapper;
    private final GroupRepository repository;
    private final PageableMapper pageableMapper;
    private final IGroupGradeService gradeService;


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public GroupDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Group> groups = repository.findAll(pageable);
        List<GroupDTO.Info> groupInfos = mapper.entityToDtoInfoList(groups.getContent());

        GroupDTO.Response response = new GroupDTO.Response();
        GroupDTO.SpecResponse specResponse = new GroupDTO.SpecResponse();

        if (groupInfos != null) {
            response.setData(groupInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) groups.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public GroupDTO.Info get(Long id) {
        Group group = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP')")
    public List<GroupGradeDTO.Info> create(GroupDTO.Create dto) {
        Group group = mapper.dtoCreateToEntity(dto);
        group = repository.save(group);
        GroupDTO.Info groupDTO = mapper.entityToDtoInfo(group);
        GroupGradeDTO.CreateAll createDto = new GroupGradeDTO.CreateAll();
        createDto.setGroupId(groupDTO.getId());
        createDto.setGradeCodes(dto.getGradeCodes());
        return gradeService.createGroupGrade(createDto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP')")
    public GroupDTO.Info update(GroupDTO.Update dto) {
        Group group = repository.findById(dto.getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(group, dto);
        Group save = repository.save(group);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP')")
    public void delete(Long id) {
        Group group = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(group);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public SearchDTO.SearchRs<GroupDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }
}

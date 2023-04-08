package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.NotFoundException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IGroupService;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class GroupService implements IGroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('')")
    public List<GroupDTO.Info> list() {
         List<Group> groups = groupRepository.findAll();
         return groupMapper.entityToDtoInfoList(groups);
    }


    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('')")
    public GroupDTO.Info get(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        GroupDTO.Info groupDtoInfo = groupMapper.entityToDtoInfo(group);
        return groupDtoInfo;
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('')")
    public TotalResponse<GroupDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(groupRepository, request, groupMapper::entityToDtoInfo);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('')")
    public GroupDTO.Info create(GroupDTO.Create dto) {
        Group group = groupMapper.dtoCreateToEntity(dto);
        group = groupRepository.save(group);
        return  groupMapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('')")
    public void delete(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        groupRepository.delete(group);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('')")
    public GroupDTO.Info update(GroupDTO.Update dto) {
        Group group =  groupRepository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        groupMapper.update(group, dto);
        Group save = groupRepository.save(group);
        return groupMapper.entityToDtoInfo(save);
    }
}

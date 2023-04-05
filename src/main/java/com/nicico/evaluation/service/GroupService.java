package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.iservice.IGroupService;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.repository.GroupRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class GroupService implements IGroupService {

    private GroupRepository groupRepository;
    private final GroupMapper groupMapper;

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
        Group group = groupRepository.findById(id).orElseThrow();
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
//    @PreAuthorize("hasAuthority('C_KPI_TYPE')")
    public GroupDTO.Info create(GroupDTO.Create dto) {
        Group group = groupMapper.dtoCreateToEntity(dto);
        group = groupRepository.save(group);
        return  groupMapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('')")
    public void delete(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        groupRepository.delete(group);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('')")
    public GroupDTO.Info update(GroupDTO.Update dto) {
        Group group =  groupRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Group not found"));
        groupMapper.update(group, dto);
        Group save = groupRepository.save(group);
        return groupMapper.entityToDtoInfo(save);
    }
}

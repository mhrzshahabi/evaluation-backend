package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.common.PageDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IGroupService;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class GroupService implements IGroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final PageableMapper pageableMapper;
    private final ApplicationException<ServiceException> applicationException;


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public PageDTO list(Pageable page) {
        Page<Group> groups = groupRepository.findAll(page);
        List<GroupDTO.Info> groupInfoDto = groupMapper.entityToDtoInfoList(groups.getContent());
        return pageableMapper.toPageDto(groups, groupInfoDto);
    }

    @Override
    public List<GroupDTO.Info> list() {
        List<Group> groupList=   groupRepository.findAll();
        return groupMapper.entityToDtoInfoList(groupList);
    }


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public GroupDTO.Info get(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return groupMapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public TotalResponse<GroupDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(groupRepository, request, groupMapper::entityToDtoInfo);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP')")
    public GroupDTO.Info create(GroupDTO.Create dto) {
        Group group = groupMapper.dtoCreateToEntity(dto);
        group = groupRepository.save(group);
        return groupMapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP')")
    public GroupDTO.Info update(GroupDTO.Update dto) {
        Group group = groupRepository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        groupMapper.update(group, dto);
        Group save = groupRepository.save(group);
        return groupMapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP')")
    public void delete(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        groupRepository.delete(group);
    }
}

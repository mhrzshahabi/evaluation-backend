package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.iservice.IGroupTypeService;
import com.nicico.evaluation.mapper.GroupTypeMapper;
import com.nicico.evaluation.model.GroupType;
import com.nicico.evaluation.repository.GroupTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional
public class GroupTypeService implements IGroupTypeService {

    private final GroupTypeRepository repository;
    private final GroupTypeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public GroupTypeDTO.Info get(Long id) throws Exception {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new Exception("exception.main-desktop.record.not.found"));
        return mapper.entityToDtoInfo(groupType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public List<GroupTypeDTO.Info> list() {
        List<GroupType> groupTypes = repository.findAll();
        return mapper.entityToDtoInfoList(groupTypes);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public TotalResponse<GroupTypeDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(repository, request, mapper::entityToDtoInfo);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP_TYPE')")
    public GroupTypeDTO.Info create(GroupTypeDTO.Create dto) {
        GroupType groupType = mapper.dtoCreateToEntity(dto);
        GroupType save = repository.save(groupType);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP_TYPE')")
    public GroupTypeDTO.Info update(GroupTypeDTO.Update dto) {
        GroupType kPIType = repository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("GroupType not  found"));
        mapper.update(kPIType, dto);
        GroupType save = repository.save(kPIType);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP_TYPE')")
    public void delete(Long id) {
        GroupType kPIType = repository.findById(id).orElseThrow(() -> new RuntimeException("GroupType not  found"));
        repository.delete(kPIType);
    }

}

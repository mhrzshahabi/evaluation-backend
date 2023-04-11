package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IGroupService;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.*;

@RequiredArgsConstructor
@Service
public class GroupService implements IGroupService {

    private final GroupMapper mapper;
    private final GroupRepository repository;
    private final PageableMapper pageableMapper;
    private final ApplicationException<ServiceException> applicationException;


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
        Group group = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return mapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP')")
    public GroupDTO.Info create(GroupDTO.Create dto) {
        Group group = mapper.dtoCreateToEntity(dto);
        group = repository.save(group);
        return mapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP')")
    public GroupDTO.Info update(GroupDTO.Update dto) {
        Group group = repository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        mapper.update(group, dto);
        Group save = repository.save(group);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP')")
    public void delete(Long id) {
        Group group = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        try {
            repository.delete(group);
        } catch (DataIntegrityViolationException violationException) {
            throw applicationException.createApplicationException(INTEGRITY_CONSTRAINT, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_DELETE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public SearchDTO.SearchRs<GroupDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }
}

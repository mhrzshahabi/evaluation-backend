package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IGroupGradeService;
import com.nicico.evaluation.mapper.GroupGradeMapper;
import com.nicico.evaluation.model.GroupGrade;
import com.nicico.evaluation.repository.GroupGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;


@RequiredArgsConstructor
@Service
public class GroupGradeService implements IGroupGradeService {

    private final GroupGradeRepository repository;
    private final GroupGradeMapper mapper;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
    //   @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public GroupGradeDTO.Info get(Long id) {
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return mapper.entityToDtoInfo(groupGrade);
    }

    @Override
    @Transactional(readOnly = true)
    // @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public List<GroupGradeDTO.Info> list() {
        List<GroupGrade> groupGrades = repository.findAll();
        return mapper.entityToDtoInfoList(groupGrades);
    }

    @Override
    @Transactional(readOnly = true)
    //  @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public TotalResponse<GroupGradeDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(repository, request, mapper::entityToDtoInfo);
    }

    @Override
    @Transactional
    //  @PreAuthorize("hasAuthority('C_GROUP_GRADE')")
    public GroupGradeDTO.Info create(GroupGradeDTO.Create dto) {
        GroupGrade groupGrade = mapper.dtoCreateToEntity(dto);
        GroupGrade save = repository.save(groupGrade);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    // @PreAuthorize("hasAuthority('U_GROUP_GRADE')")
    public GroupGradeDTO.Info update(GroupGradeDTO.Update dto) {
        GroupGrade groupGrade = repository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        mapper.update(groupGrade, dto);
        GroupGrade save = repository.save(groupGrade);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    //  @PreAuthorize("hasAuthority('D_GROUP_GRADE')")
    public void delete(Long id) {
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.delete(groupGrade);
    }

}

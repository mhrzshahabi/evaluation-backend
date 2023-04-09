package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IGradeService;
import com.nicico.evaluation.iservice.IGroupGradeService;
import com.nicico.evaluation.mapper.GroupGradeMapper;
import com.nicico.evaluation.model.GroupGrade;
import com.nicico.evaluation.repository.GroupGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;


@RequiredArgsConstructor
@Service
public class GroupGradeService implements IGroupGradeService {

    private final GroupGradeRepository repository;
    private final GroupGradeMapper mapper;
    private final ApplicationException<ServiceException> applicationException;
    private final IGradeService gradeService;

    @Override
    @Transactional(readOnly = true)
    //   @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public GroupGradeDTO.Info get(Long id) {
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        GradeDTO.Info gradeDTO = gradeService.getByCode(groupGrade.getGradeCode());
        GroupGradeDTO.Info groupGradeDTO = mapper.entityToDtoInfo(groupGrade);
        groupGradeDTO.setGrade(gradeDTO);
        return groupGradeDTO;
    }

    @Override
    @Transactional(readOnly = true)
    // @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public List<GroupGradeDTO.Info> list() {
        List<GroupGrade> groupGrades = repository.findAll();
        List<GroupGradeDTO.Info> infos = mapper.entityToDtoInfoList(groupGrades);
        infos.forEach(groupGrade -> {
            GradeDTO.Info gradeDTO = gradeService.getAllByCodeIn(Collections.singletonList(groupGrade.getGradeCode())).get(0);
            groupGrade.setGrade(gradeDTO);
        });
        return infos;
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
    public List<GroupGradeDTO.Info> createAll(List<GroupGradeDTO.Create> requests) {
        return requests.stream().map(this::create).toList();
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
    //  @PreAuthorize("hasAuthority('C_GROUP_GRADE')")
    public List<GroupGradeDTO.Info> createGroupGrade(GroupGradeDTO.CreateAll dto) {
        List<GradeDTO.Info> grades = gradeService.getAllByCodeIn(dto.getGradeCodes());
        List<GroupGradeDTO.Create> createAllDto = new ArrayList<>();
        grades.forEach(grade -> {
            GroupGradeDTO.Create createDto = new GroupGradeDTO.Create();
            createDto.setGradeCode(grade.getCode());
            createDto.setGradeId(grade.getId());
            createDto.setGroupId(dto.getGroupId());
            createDto.setTitle(dto.getTitle());
            createDto.setCode(dto.getCode());

            createAllDto.add(createDto);
        });
        return this.createAll(createAllDto);
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

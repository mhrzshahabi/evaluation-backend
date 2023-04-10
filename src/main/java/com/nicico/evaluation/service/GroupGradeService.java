package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.common.PageableMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.nicico.evaluation.exception.CoreException.*;


@RequiredArgsConstructor
@Service
public class GroupGradeService implements IGroupGradeService {

    private final GroupGradeMapper mapper;
    private final IGradeService gradeService;
    private final PageableMapper pageableMapper;
    private final GroupGradeRepository repository;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public GroupGradeDTO.Info get(Long id) {
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return mapper.entityToDtoInfo(groupGrade);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public GroupGradeDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<GroupGrade> groupGrades = repository.findAll(pageable);
        List<GroupGradeDTO.Info> groupGradeInfos = mapper.entityToDtoInfoList(groupGrades.getContent());

        GroupGradeDTO.Response response = new GroupGradeDTO.Response();
        GroupGradeDTO.SpecResponse specResponse = new GroupGradeDTO.SpecResponse();

        if (groupGradeInfos != null) {
            response.setData(groupGradeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) groupGrades.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public TotalResponse<GroupGradeDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(repository, request, mapper::entityToDtoInfo);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP_GRADE')")
    public List<GroupGradeDTO.Info> createAll(List<GroupGradeDTO.Create> requests) {
        return requests.stream().map(this::create).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP_GRADE')")
    public GroupGradeDTO.Info create(GroupGradeDTO.Create dto) {
        GroupGrade groupGrade = mapper.dtoCreateToEntity(dto);
        try {
            GroupGrade save = repository.save(groupGrade);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP_GRADE')")
    public List<GroupGradeDTO.Info> createGroupGrade(GroupGradeDTO.CreateAll dto) {
        List<GradeDTO.Info> grades = gradeService.getAllByCodeIn(dto.getGradeCodes());

        Boolean validation = createValidation(grades);
        if (Boolean.FALSE.equals(validation))
            throw applicationException.createApplicationException(GRADE_IS_IN_ANOTHER_GROUP, HttpStatus.CONFLICT);

        List<GroupGradeDTO.Create> createAllDto = new ArrayList<>();
        grades.forEach(grade -> {
            GroupGradeDTO.Create createDto = new GroupGradeDTO.Create();
            createDto.setGradeId(grade.getId());
            createDto.setCode(grade.getCode());
            createDto.setTitle(grade.getTitle());
            createDto.setGroupId(dto.getGroupId());

            createAllDto.add(createDto);
        });
        return this.createAll(createAllDto);
    }

    private Boolean createValidation(List<GradeDTO.Info> grades) {
        List<Long> gradeIds = grades.stream().map(GradeDTO::getId).toList();
        List<GroupGrade> allByGradeIdIn = repository.findAllByGradeIdIn(gradeIds);
        return allByGradeIdIn.isEmpty();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP_GRADE')")
    public List<GroupGradeDTO.Info> update(Long id, GroupGradeDTO.CreateAll dto) {
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        List<Long> groupGradeIds = repository.findAllByGroupId(groupGrade.getGroupId()).stream().map(GroupGrade::getId).toList();
        this.deleteAll(groupGradeIds);
        return this.createGroupGrade(dto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP_GRADE')")
    public void deleteAll(List<Long> ids) {
        ids.forEach(this::delete);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP_GRADE')")
    public void delete(Long id) {
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.delete(groupGrade);
    }

}

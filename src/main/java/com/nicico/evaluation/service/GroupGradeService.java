package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGradeService;
import com.nicico.evaluation.iservice.IGroupGradeService;
import com.nicico.evaluation.mapper.GroupGradeMapper;
import com.nicico.evaluation.model.Grade;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.model.GroupGrade;
import com.nicico.evaluation.repository.GroupGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupGradeService implements IGroupGradeService {

    private final GroupGradeMapper mapper;
    private final IGradeService gradeService;
    private final PageableMapper pageableMapper;
    private final GroupGradeRepository repository;
    private final ResourceBundleMessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public GroupGradeDTO.Info get(Long id) {
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
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
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP_GRADE')")
    public List<GroupGradeDTO.Info> createGroupGrade(GroupGradeDTO.CreateAll dto) {
        List<GradeDTO.Info> grades = gradeService.getAllByCodeIn(dto.getGradeCodes());
        final Locale locale = LocaleContextHolder.getLocale();
        Boolean validation = createValidation(grades);
        if (Boolean.FALSE.equals(validation))
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.GradeIsInAnotherGroup, null, messageSource.getMessage("exception.grade.is.in.another.group", null, locale));

        List<GroupGradeDTO.Create> createAllDto = new ArrayList<>();
        grades.forEach(grade -> {
            GroupGradeDTO.Create createDto = new GroupGradeDTO.Create();
            createDto.setGradeId(grade.getId());
            createDto.setGradeCode(grade.getCode());
            createDto.setGradeTitle(grade.getTitle());
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
    public List<GroupGradeDTO.Info> update(GroupGradeDTO.CreateAll dto) {
        List<Long> groupGradeIds = repository.findAllByGroupId(dto.getGroupId()).stream().map(GroupGrade::getId).toList();
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
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(groupGrade);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_GRADE')")
    public SearchDTO.SearchRs<GroupGradeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    public GroupGradeDTO.Info getGroupGradeByGrade(Grade grade) {
        Optional<GroupGrade> optionalGroupGrade = repository.findFirstByGradeCode(grade.getCode());
        return optionalGroupGrade.map(mapper::entityToDtoInfo).orElse(null);

    }
    @Override
    public List<GroupGradeDTO.Info> getGroupGradeByGroup(Group group) {
        List<GroupGrade> allByGroupId = repository.findAllByGroupId(group.getId());
        return allByGroupId.stream().map(mapper::entityToDtoInfo).toList();

    }

}

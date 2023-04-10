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

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;


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
        GradeDTO.Info gradeDTO = gradeService.getByCode(groupGrade.getGradeCode());
        GroupGradeDTO.Info groupGradeDTO = mapper.entityToDtoInfo(groupGrade);
        groupGradeDTO.setGrade(gradeDTO);
        return groupGradeDTO;
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
    public GroupGradeDTO.Info create(GroupGradeDTO.Create dto) {
        GroupGrade groupGrade = mapper.dtoCreateToEntity(dto);
        GroupGrade save = repository.save(groupGrade);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
     @PreAuthorize("hasAuthority('U_GROUP_GRADE')")
    public GroupGradeDTO.Info update(GroupGradeDTO.Update dto) {
        GroupGrade groupGrade = repository.findById(dto.getId()).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        mapper.update(groupGrade, dto);
        GroupGrade save = repository.save(groupGrade);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
      @PreAuthorize("hasAuthority('D_GROUP_GRADE')")
    public void delete(Long id) {
        GroupGrade groupGrade = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.delete(groupGrade);
    }

}

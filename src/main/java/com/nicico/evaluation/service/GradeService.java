package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGradeService;
import com.nicico.evaluation.mapper.GradeMapper;
import com.nicico.evaluation.model.Grade;
import com.nicico.evaluation.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GradeService implements IGradeService {

    private final GradeMapper mapper;
    private final GradeRepository repository;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GRADE')")
    public GradeDTO.Info get(Long id) {
        Grade grade = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(grade);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GRADE')")
    public List<GradeDTO.Info> getAllByCodeIn(List<String> codes) {
        List<Grade> grades = repository.getAllByCodeIn(codes);
        return mapper.entityToDtoInfoList(grades);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GRADE')")
    public GradeDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Grade> grades = repository.findAll(pageable);
        List<GradeDTO.Info> gradeInfos = mapper.entityToDtoInfoList(grades.getContent());
        GradeDTO.Response response = new GradeDTO.Response();
        GradeDTO.SpecResponse specResponse = new GradeDTO.SpecResponse();

        if (gradeInfos != null) {
            response.setData(gradeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) grades.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GRADE')")
    public SearchDTO.SearchRs<GradeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GRADE')")
    public SearchDTO.SearchRs<GradeDTO.Info> searchWithoutGroup(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }


}

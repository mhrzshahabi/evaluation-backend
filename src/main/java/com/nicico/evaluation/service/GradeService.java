package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.IGradeService;
import com.nicico.evaluation.mapper.GradeMapper;
import com.nicico.evaluation.model.Grade;
import com.nicico.evaluation.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;


@RequiredArgsConstructor
@Service
public class GradeService implements IGradeService {

    private final GradeRepository repository;
    private final GradeMapper mapper;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_GRADE')")
    public GradeDTO.Info get(Long id) throws Exception {
        Grade grade = repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return mapper.entityToDtoInfo(grade);
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_GRADE')")
    public List<GradeDTO.Info> list() {
        List<Grade> gradeList = repository.findAll();
        return mapper.entityToDtoInfoList(gradeList);
    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_GRADE')")
    public TotalResponse<GradeDTO.Info> search(NICICOCriteria request) {
        return SearchUtil.search(repository, request, mapper::entityToDtoInfo);
    }
}

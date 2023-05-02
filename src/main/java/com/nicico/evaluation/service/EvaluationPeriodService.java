package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationPeriodService;
import com.nicico.evaluation.mapper.EvaluationPeriodMapper;
import com.nicico.evaluation.model.EvaluationPeriod;
import com.nicico.evaluation.repository.EvaluationPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EvaluationPeriodService implements IEvaluationPeriodService {

    private final EvaluationPeriodMapper evaluationPeriodMapper;
    private final EvaluationPeriodRepository evaluationPeriodRepository;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.Info get(Long id) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return evaluationPeriodMapper.entityToDtoInfo(evaluationPeriod);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<EvaluationPeriod> evaluationPeriods = evaluationPeriodRepository.findAll(pageable);
        List<EvaluationPeriodDTO.Info> evaluationInfos = evaluationPeriodMapper.entityToDtoInfoList(evaluationPeriods.getContent());

        EvaluationPeriodDTO.Response response = new EvaluationPeriodDTO.Response();
        EvaluationPeriodDTO.SpecResponse specResponse = new EvaluationPeriodDTO.SpecResponse();

        if (evaluationInfos != null) {
            response.setData(evaluationInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) evaluationPeriods.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public SearchDTO.SearchRs<EvaluationPeriodDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(evaluationPeriodRepository, evaluationPeriodMapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.Info create(EvaluationPeriodDTO.Create dto) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodMapper.dtoCreateToEntity(dto);
        try {
            if (
                    evaluationPeriod.getStartDateArzeshYabi().compareTo(evaluationPeriod.getStartDate()) > 0 &&
                            evaluationPeriod.getStartDateArzeshYabi().compareTo(evaluationPeriod.getEndDate()) < 0 &&
                            evaluationPeriod.getEndDateArzeshYabi().compareTo(evaluationPeriod.getStartDate()) > 0 &&
                            evaluationPeriod.getEndDateArzeshYabi().compareTo(evaluationPeriod.getEndDate()) < 0 &&
                            evaluationPeriod.getEndDateArzeshYabi().compareTo(evaluationPeriod.getStartDateArzeshYabi()) > 0
            ) {
                EvaluationPeriod save = evaluationPeriodRepository.save(evaluationPeriod);
                return evaluationPeriodMapper.entityToDtoInfo(save);
            }
            throw new Exception();
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.Info update(EvaluationPeriodDTO.Update dto) {
        try {
            if (
                    dto.getStartDateArzeshYabi().compareTo(dto.getStartDate()) > 0 &&
                            dto.getStartDateArzeshYabi().compareTo(dto.getEndDate()) < 0 &&
                            dto.getEndDateArzeshYabi().compareTo(dto.getStartDate()) > 0 &&
                            dto.getEndDateArzeshYabi().compareTo(dto.getEndDate()) < 0 &&
                            dto.getEndDateArzeshYabi().compareTo(dto.getStartDateArzeshYabi()) > 0
            ) {
                EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(dto.getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
                evaluationPeriodMapper.update(evaluationPeriod, dto);
                EvaluationPeriod save = evaluationPeriodRepository.save(evaluationPeriod);
                return evaluationPeriodMapper.entityToDtoInfo(save);
            }
            throw new Exception();
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION_PERIOD')")
    public void delete(Long id) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        evaluationPeriodRepository.delete(evaluationPeriod);
    }
}

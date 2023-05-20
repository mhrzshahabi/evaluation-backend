package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationService;
import com.nicico.evaluation.mapper.EvaluationMapper;
import com.nicico.evaluation.model.Evaluation;
import com.nicico.evaluation.repository.EvaluationRepository;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EvaluationService implements IEvaluationService {

    private final EvaluationMapper mapper;
    private final EvaluationRepository repository;
    private final PageableMapper pageableMapper;

    @Override
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست آیتم های ارزیابی ها");
        return new ExcelGenerator.ExcelDownload(body);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Evaluation> Evaluations = repository.findAll(pageable);
        List<EvaluationDTO.Info> EvaluationInfos = mapper.entityToDtoInfoList(Evaluations.getContent());

        EvaluationDTO.Response response = new EvaluationDTO.Response();
        EvaluationDTO.SpecResponse specResponse = new EvaluationDTO.SpecResponse();

        if (EvaluationInfos != null) {
            response.setData(EvaluationInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) Evaluations.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.Info get(Long id) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(Evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public Evaluation getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION')")
    public EvaluationDTO.Info create(EvaluationDTO.Create dto) {
        Evaluation Evaluation = mapper.dtoCreateToEntity(dto);
        Evaluation = repository.save(Evaluation);
        return mapper.entityToDtoInfo(Evaluation);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION')")
    public EvaluationDTO.Info update(Long id, EvaluationDTO.Update dto) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(Evaluation, dto);
        Evaluation save = repository.save(Evaluation);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION')")
    public EvaluationDTO.Info update(Long id, Evaluation evaluation) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        EvaluationDTO.Update evaluationDTO = mapper.entityToUpdateDto(evaluation);
        mapper.update(Evaluation, evaluationDTO);
        Evaluation save = repository.save(Evaluation);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION')")
    public void delete(Long id) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(Evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}

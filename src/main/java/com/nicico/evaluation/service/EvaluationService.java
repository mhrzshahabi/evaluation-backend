package com.nicico.evaluation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationService;
import com.nicico.evaluation.mapper.EvaluationMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.model.Evaluation;
import com.nicico.evaluation.repository.CatalogRepository;
import com.nicico.evaluation.repository.EvaluationRepository;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EvaluationService implements IEvaluationService {

    private final EvaluationMapper mapper;
    private final EvaluationRepository repository;
    private final PageableMapper pageableMapper;
    private final CatalogRepository catalogRepository;
    private final ResourceBundleMessageSource messageSource;

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

    @Override
    @Transactional
    public BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO) {
        BaseResponse response = new BaseResponse();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            changeStatusDTO.getEvaluationIds().forEach(id->{
                Optional<Evaluation> optionalEvaluation = repository.findById(id);
                if (optionalEvaluation.isPresent()){
                    Evaluation evaluation = optionalEvaluation.get();
                    switch (changeStatusDTO.getStatus().toLowerCase(Locale.ROOT)) {
                        case "next" -> {
                            if (evaluation.getStatusCatalog().getCode()!=null && evaluation.getStatusCatalog().getCode().equals("Initial-registration")) {
                                Optional<Catalog> optionalCatalog = catalogRepository.findByCode("Awaiting-review");
                                optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                            }
                            else if (evaluation.getStatusCatalog().getCode()!=null && evaluation.getStatusCatalog().getCode().equals("Awaiting-review")) {
                                Optional<Catalog> optionalCatalog = catalogRepository.findByCode("Finalized");
                                optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                            }
                            repository.save(evaluation);
                        }
                        case "previous" ->{
                            if (evaluation.getStatusCatalog().getCode()!=null && evaluation.getStatusCatalog().getCode().equals("Finalized")) {
                                Optional<Catalog> optionalCatalog = catalogRepository.findByCode("Awaiting-review");
                                optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                            }
                            else if (evaluation.getStatusCatalog().getCode()!=null && evaluation.getStatusCatalog().getCode().equals("Awaiting-review")) {
                                Optional<Catalog> optionalCatalog = catalogRepository.findByCode("Initial-registration");
                                optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));

                            }
                            repository.save(evaluation);
                        }
                    }
                }
            });
            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
            response.setStatus(200);
            return response;
        }catch (Exception e){
            response.setMessage(messageSource.getMessage("exception.un-managed", null, locale));
            response.setStatus(EvaluationHandleException.ErrorType.EvaluationDeadline.getHttpStatusCode());
            return response;
        }
    }

}

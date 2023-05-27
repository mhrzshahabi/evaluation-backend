package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationPeriodPostService;
import com.nicico.evaluation.iservice.IEvaluationPeriodService;
import com.nicico.evaluation.mapper.EvaluationPeriodMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.model.Evaluation;
import com.nicico.evaluation.model.EvaluationPeriod;
import com.nicico.evaluation.repository.CatalogRepository;
import com.nicico.evaluation.repository.EvaluationPeriodRepository;
import com.nicico.evaluation.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class EvaluationPeriodService implements IEvaluationPeriodService {

    private final PageableMapper pageableMapper;
    private final EvaluationPeriodMapper evaluationPeriodMapper;
    private final EvaluationPeriodRepository evaluationPeriodRepository;
    private final IEvaluationPeriodPostService evaluationPeriodPostService;
    private final ICatalogService catalogService;
    private final CatalogRepository catalogRepository;
    private final ResourceBundleMessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod get(Long id) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> postInfoEvaluationPeriods = evaluationPeriodPostService.getAllByEvaluationPeriodId(id);
        EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod evaluationPeriodInfoPost = evaluationPeriodMapper.entityToDtoInfoWithPostInfoEvaluationPeriod(evaluationPeriod);
        evaluationPeriodInfoPost.setPostInfoEvaluationPeriod(postInfoEvaluationPeriods);
        return evaluationPeriodInfoPost;
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
    public List<EvaluationPeriodPostDTO.Info> createEvaluationPeriodPost(Long id, Set<String> postCode) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        List<EvaluationPeriodPostDTO.Info> evaluationPeriodPostInfos = evaluationPeriodPostService.createAll(evaluationPeriod, postCode);
        return evaluationPeriodPostInfos;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION_PERIOD')")
    public void deleteEvaluationPeriodPost(Long id, String postCode) {
        evaluationPeriodPostService.deleteByEvaluationPeriodIdAndPostCode(id, postCode);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.Info create(EvaluationPeriodDTO.Create dto) {
        try {
            if (this.isValidDates(dto.getStartDate(), dto.getEndDate(),
                    dto.getStartDateAssessment(), dto.getEndDateAssessment())
            ) {
                EvaluationPeriod evaluationPeriod = evaluationPeriodMapper.dtoCreateToEntity(dto);
                evaluationPeriod.setStatusCatalogId(catalogService.getByCode("period-initial-registration").getId());
                EvaluationPeriod save = evaluationPeriodRepository.save(evaluationPeriod);
                if(dto.getPostCode() != null && !dto.getPostCode().isEmpty()) {
                    evaluationPeriodPostService.createAll(save, dto.getPostCode());
                }
                return evaluationPeriodMapper.entityToDtoInfo(save);
            } else
                throw new Exception("NotInEvalPeriod");
        } catch (Exception exception) {
            if(exception.getMessage().equals("NotInEvalPeriod")){
                String errorMessage = messageSource.getMessage("message.notinevaluationperiod.evaluation.period", null, LocaleContextHolder.getLocale());
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave, null, errorMessage);
            }
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.Info update(Long id, EvaluationPeriodDTO.Update dto) {
        try {
            if (this.isValidDates(dto.getStartDate(), dto.getEndDate(), dto.getStartDateAssessment(), dto.getEndDateAssessment())) {
                EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
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
        evaluationPeriodPostService.deleteByEvaluationPeriodId(evaluationPeriod.getId());
        evaluationPeriodRepository.delete(evaluationPeriod);
    }

    @Override
    @Transactional
    public BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO) {
        BaseResponse response = new BaseResponse();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            List<Long> ids = changeStatusDTO.getEvaluationIds();
            for (Long id : ids) {
                Optional<EvaluationPeriod> optionalEvaluationPeriod = evaluationPeriodRepository.findById(id);
                if (optionalEvaluationPeriod.isPresent()) {
                    EvaluationPeriod evaluationPeriod = optionalEvaluationPeriod.get();
                        switch (changeStatusDTO.getStatus().toLowerCase(Locale.ROOT)) {
                            case "next" -> {
                                if (evaluationPeriod.getStatusCatalog().getCode() != null && evaluationPeriod.getStatusCatalog().getCode().equals("period-initial-registration")) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode("period-awaiting-review");
                                    optionalCatalog.ifPresent(catalog -> evaluationPeriod.setStatusCatalogId(catalog.getId()));
                                } else if (evaluationPeriod.getStatusCatalog().getCode() != null && evaluationPeriod.getStatusCatalog().getCode().equals("period-awaiting-review")) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode("period-finalized");
                                    optionalCatalog.ifPresent(catalog -> evaluationPeriod.setStatusCatalogId(catalog.getId()));
                                }
                                evaluationPeriodRepository.save(evaluationPeriod);
                            }
                            case "previous" -> {
                                if (evaluationPeriod.getStatusCatalog().getCode() != null && evaluationPeriod.getStatusCatalog().getCode().equals("period-finalized")) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode("period-awaiting-review");
                                    optionalCatalog.ifPresent(catalog -> evaluationPeriod.setStatusCatalogId(catalog.getId()));
                                } else if (evaluationPeriod.getStatusCatalog().getCode() != null && evaluationPeriod.getStatusCatalog().getCode().equals("period-awaiting-review")) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode("period-initial-registration");
                                    optionalCatalog.ifPresent(catalog -> evaluationPeriod.setStatusCatalogId(catalog.getId()));

                                }
                                evaluationPeriodRepository.save(evaluationPeriod);
                            }
                        }

                }
            }
            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
            response.setStatus(200);
            return response;
        } catch (Exception e) {
            response.setMessage(messageSource.getMessage("exception.un-managed", null, locale));
            response.setStatus(EvaluationHandleException.ErrorType.EvaluationDeadline.getHttpStatusCode());
            return response;
        }
    }


    private boolean isValidDates(Date startDate, Date endDate, Date startDateAssessment, Date endDateAssessment) {
        return startDateAssessment.compareTo(startDate) >= 0 &&
                startDateAssessment.compareTo(endDate) < 0 &&
                startDateAssessment.compareTo(endDateAssessment) < 0 &&
                endDateAssessment.compareTo(startDate) > 0 &&
                endDateAssessment.compareTo(endDate) <= 0 &&
                endDateAssessment.compareTo(startDateAssessment) > 0;
    }
}

package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationService;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.iservice.ISpecialCaseService;
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

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class EvaluationService implements IEvaluationService {

    private final EvaluationMapper mapper;
    private final EvaluationRepository repository;
    private final PageableMapper pageableMapper;
    private final CatalogRepository catalogRepository;
    private final ResourceBundleMessageSource messageSource;
    private final ISpecialCaseService specialCaseService;
    private final IOrganizationTreeService organizationTreeService;


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
    @PreAuthorize("hasAuthority('C_EVALUATION')")
    public List<EvaluationDTO.Info> createList(List<EvaluationDTO.Create> dto) {
        List<EvaluationDTO.Info> evaluationInfo = new ArrayList<>();
        List<Evaluation> evaluations = mapper.dtoCreateToEntityList(dto);
        for (Evaluation e : evaluations) {
            List<Evaluation> evaluationList =
                    repository.findByEvaluationPeriodIdAndAssessPostCodeAndEndDate(e.getEvaluationPeriodId(), e.getAssessPostCode(), e.getEndDate());
            if (evaluationList.size() > 0) {
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
            }
            SpecialCaseDTO.Info scInfo = specialCaseService.getByAssessNationalCodeAndAssessPostCode(e.getAssessNationalCode(), e.getAssessPostCode());
            if (scInfo != null) {
                e.setAssessorPostCode(scInfo.getAssessorPostCode());
                e.setAssessorNationalCode(scInfo.getAssessorNationalCode());
                e.setAssessorFullName(scInfo.getAssessorFullName());
                e.setAssessFullName(scInfo.getAssessFullName());
            } else {
                OrganizationTreeDTO.Info organizationTreeInfo = organizationTreeService.getByPostCodeAndNationalCode(e.getAssessPostCode(), e.getAssessNationalCode());
                e.setAssessorPostCode(organizationTreeInfo.getPostParentCode());
                e.setAssessorNationalCode(organizationTreeInfo.getNationalCodeParent());
                e.setAssessorFullName(organizationTreeInfo.getFirstNameParent() + " " + organizationTreeInfo.getLastNameParent());
                e.setAssessFullName(organizationTreeInfo.getFullName());
            }
            e = repository.save(e);
            evaluationInfo.add(mapper.entityToDtoInfo(e));
        }
        return evaluationInfo;
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

    @Override
    @Transactional
    public BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO) {
        BaseResponse response = new BaseResponse();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            List<Long> ids = changeStatusDTO.getEvaluationIds();
            for (Long id : ids) {
                Optional<Evaluation> optionalEvaluation = repository.findById(id);
                if (optionalEvaluation.isPresent()) {
                    Evaluation evaluation = optionalEvaluation.get();
                    String miDate = DateUtil.convertKhToMi1(evaluation.getEvaluationPeriod().getEndDate());
                    Date evaluationDate = new SimpleDateFormat("yyyy-MM-dd").parse(miDate);
                    if (evaluationDate != null && evaluationDate.before(new Date())) {
                        switch (changeStatusDTO.getStatus().toLowerCase(Locale.ROOT)) {
                            case "next" -> {
                                if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals("Initial-registration")) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode("Awaiting-review");
                                    optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                } else if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals("Awaiting-review")) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode("Finalized");
                                    optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                }
                                repository.save(evaluation);
                            }
                            case "previous" -> {
                                if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals("Finalized")) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode("Awaiting-review");
                                    optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                } else if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals("Awaiting-review")) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode("Initial-registration");
                                    optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));

                                }
                                repository.save(evaluation);
                            }
                        }
                    } else {
                        throw new Exception();
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

}

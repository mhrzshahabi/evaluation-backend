
package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.PersonDTO;
import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IPersonService;
import com.nicico.evaluation.iservice.ISpecialCaseService;
import com.nicico.evaluation.mapper.SpecialCaseMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.model.SpecialCase;
import com.nicico.evaluation.repository.CatalogRepository;
import com.nicico.evaluation.repository.SpecialCaseRepository;
import com.nicico.evaluation.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.nicico.evaluation.utility.EvaluationConstant.*;

@RequiredArgsConstructor
@Service
public class SpecialCaseService implements ISpecialCaseService {

    private final SpecialCaseRepository repository;
    private final SpecialCaseMapper mapper;
    private final PageableMapper pageableMapper;
    private final ResourceBundleMessageSource messageSource;
    private final CatalogRepository catalogRepository;
    private final ICatalogService catalogService;
    private final IPersonService personService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public SpecialCaseDTO.Info get(Long id) {
        SpecialCase specialcase = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(specialcase);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public List<SpecialCaseDTO.Info> getByAssessNationalCodeAndStatusCode(String nationalCode, String statusCode) {
        List<SpecialCase> specialCases = repository.findByAssessNationalCodeAndStatusCatalogId(nationalCode,
                catalogService.getByCode(statusCode).getId());
        return mapper.entityToDtoInfoList(specialCases);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public List<SpecialCaseDTO.Info> getByAssessNationalCodeAndStatusCodeNotIn(String nationalCode, String statusCode, List<Long> id) {
        List<SpecialCase> specialCases = repository.findByAssessNationalCodeAndStatusCatalogIdAndIdNotIn(nationalCode,
                catalogService.getByCode(statusCode).getId(), id);
        return mapper.entityToDtoInfoList(specialCases);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public SpecialCaseDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<SpecialCase> specialCases = repository.findAll(pageable);
        List<SpecialCaseDTO.Info> specialCaseInfos = mapper.entityToDtoInfoList(specialCases.getContent());

        SpecialCaseDTO.Response response = new SpecialCaseDTO.Response();
        SpecialCaseDTO.SpecResponse specResponse = new SpecialCaseDTO.SpecResponse();

        if (specialCaseInfos != null) {
            response.setData(specialCaseInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) specialCases.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public SearchDTO.SearchRs<SpecialCaseDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_SPECIAL_CASE')")
    public SpecialCaseDTO.Info create(SpecialCaseDTO.Create dto) {
        validate(dto, 0L);
        SpecialCase specialCase = mapper.dtoCreateToEntity(dto);
        try {
            specialCase.setStatusCatalogId(catalogService.getByCode(SPECIAL_INITIAL_REGISTRATION).getId());
            specialCase = repository.save(specialCase);
            return mapper.entityToDtoInfo(specialCase);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    public BaseResponse batchCreate(SpecialCaseDTO.BatchCreate dto) {
        BaseResponse response = new BaseResponse();
        try {
            List<PersonDTO.Info> allByNationalCode = personService.getAllByNationalCode(Arrays.asList(dto.getAssessNationalCode(), dto.getAssessorNationalCode()));
            Optional<PersonDTO.Info> assess = allByNationalCode.stream().filter(person -> person.getNationalCode().equals(dto.getAssessNationalCode())).findFirst();
            Optional<PersonDTO.Info> assessor = allByNationalCode.stream().filter(person -> person.getNationalCode().equals(dto.getAssessorNationalCode())).findFirst();
            SpecialCaseDTO.Create createDto = new SpecialCaseDTO.Create();
            if (assess.isPresent() && assessor.isPresent()) {
                createDto.setAssessFullName(assess.get().getFullName());
                createDto.setAssessNationalCode(assess.get().getNationalCode());
                createDto.setAssessPostCode(assess.get().getPostCode());
                createDto.setAssessorFullName(assessor.get().getFullName());
                createDto.setAssessorNationalCode(assessor.get().getNationalCode());
                createDto.setAssessorPostCode(assessor.get().getPostCode());
                createDto.setAssessRealPostCode(assessor.get().getPostCode());
                createDto.setStartDate(dto.getStartDate());
                createDto.setEndDate(dto.getEndDate());
            }
            create(createDto);
            response.setStatus(HttpStatus.OK.value());

        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage(exception.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_SPECIAL_CASE')")
    public SpecialCaseDTO.Info update(Long id, SpecialCaseDTO.Update dto) {
        validate(dto, id);
        SpecialCase specialcase = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(specialcase, dto);
        try {
            SpecialCase save = repository.save(specialcase);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    private void validate(SpecialCaseDTO dto, long id) {

        if (Objects.nonNull(dto.getAssessorNationalCode()) && dto.getAssessorNationalCode().equals(dto.getAssessNationalCode())) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "assessNationalCode and assessorNationalCode",
                    messageSource.getMessage("exception.nationalCode.duplicate", null, locale));
        }
        List<SpecialCaseDTO.Info> byAssessNationalCodeAndStatusCode = getByAssessNationalCodeAndStatusCodeNotIn(dto.getAssessNationalCode(), SPECIAL_ACTIVE, Collections.singletonList(id));

        if (!byAssessNationalCodeAndStatusCode.isEmpty())
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "assessNationalCode and active status is exist",
                    messageSource.getMessage("exception.nationalCode.active.status.duplicate", null, LocaleContextHolder.getLocale()));

        if (dto.getStartDate().after(dto.getEndDate())) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "stareDate",
                    messageSource.getMessage("exception.start.date.after.end.date", null, LocaleContextHolder.getLocale()));
        }
        if (Objects.isNull(dto.getAssessorPostCode()) && Objects.isNull(dto.getAssessRealPostCode())) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    null,
                    messageSource.getMessage("exception.special-case.cant.null.assessor.and.assess-real-post", null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_SPECIAL_CASE')")
    public void delete(Long id) {
        SpecialCase specialcase = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(specialcase);
    }

    @Override
    @Transactional
    public BaseResponse changeAllStatus(SpecialCaseDTO.ChangeAllStatusDTO changeAllStatusDTO) {
        BaseResponse response = new BaseResponse();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            List<Long> ids = changeAllStatusDTO.getSpecialCaseIds();
            ids.forEach(id -> {
                SpecialCaseDTO.ChangeStatusDTO changeStatusDTO = new SpecialCaseDTO.ChangeStatusDTO();
                changeStatusDTO.setId(id);
                changeStatus(changeStatusDTO);
            });
            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
            response.setStatus(200);
            return response;
        } catch (Exception e) {
            response.setMessage(messageSource.getMessage("exception.un-managed", null, locale));
            response.setStatus(EvaluationHandleException.ErrorType.EvaluationDeadline.getHttpStatusCode());
            return response;
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void revokedExpireSpecialCase() {
        Long revokedStatusId = catalogService.getByCode(SPECIAL_REVOKED).getId();
        List<Long> expireIds = repository.findAllExpireSpecialCase(DateUtil.todayDate(), revokedStatusId).stream().map(SpecialCase::getId).toList();
        if (!expireIds.isEmpty()) {
            SpecialCaseDTO.ChangeAllStatusDTO changeAllStatusDTO = new SpecialCaseDTO.ChangeAllStatusDTO();
            changeAllStatusDTO.setSpecialCaseIds(expireIds);
            changeAllStatus(changeAllStatusDTO);
        }
    }

    @Override
    @Transactional
    public BaseResponse changeStatus(SpecialCaseDTO.ChangeStatusDTO changeStatusDTO) {
        BaseResponse response = new BaseResponse();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            Long id = changeStatusDTO.getId();

            Optional<SpecialCase> optionalSpecialCase = repository.findById(id);
            if (optionalSpecialCase.isPresent()) {
                SpecialCase specialCase = optionalSpecialCase.get();

                if (Objects.nonNull(specialCase.getStatusCatalog().getCode()) && specialCase.getStatusCatalog().getCode().equals(SPECIAL_INITIAL_REGISTRATION)) {
                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode(SPECIAL_ACTIVE);
                    optionalCatalog.ifPresent(catalog -> specialCase.setStatusCatalogId(catalog.getId()));
                } else if (Objects.nonNull(specialCase.getStatusCatalog().getCode()) && specialCase.getStatusCatalog().getCode().equals(SPECIAL_ACTIVE)) {
                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode(SPECIAL_REVOKED);
                    optionalCatalog.ifPresent(catalog -> specialCase.setStatusCatalogId(catalog.getId()));
                }
                repository.save(specialCase);
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
    

package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.nicico.evaluation.utility.EvaluationConstant.*;

@RequiredArgsConstructor
@Service
public class SpecialCaseService implements ISpecialCaseService {

    private final SpecialCaseRepository specialCaseRepository;
    private final SpecialCaseMapper specialCaseMapper;
    private final PageableMapper pageableMapper;
    private final ResourceBundleMessageSource messageSource;
    private final CatalogRepository catalogRepository;
    private final ICatalogService catalogService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public SpecialCaseDTO.Info get(Long id) {
        SpecialCase specialcase = specialCaseRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return specialCaseMapper.entityToDtoInfo(specialcase);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public List<SpecialCaseDTO.Info> getByAssessNationalCodeAndStatusCode(String nationalCode, String statusCode) {
        List<SpecialCase> specialCases = specialCaseRepository.findByAssessNationalCodeAndStatusCatalogId(nationalCode,
                catalogService.getByCode(statusCode).getId());
        return specialCaseMapper.entityToDtoInfoList(specialCases);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public List<SpecialCaseDTO.Info> getByAssessNationalCodeAndStatusCodeNotIn(String nationalCode, String statusCode, List<Long> id) {
        List<SpecialCase> specialCases = specialCaseRepository.findByAssessNationalCodeAndStatusCatalogIdAndIdNotIn(nationalCode,
                catalogService.getByCode(statusCode).getId(), id);
        return specialCaseMapper.entityToDtoInfoList(specialCases);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_SPECIAL_CASE')")
    public SpecialCaseDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<SpecialCase> specialCases = specialCaseRepository.findAll(pageable);
        List<SpecialCaseDTO.Info> specialCaseInfos = specialCaseMapper.entityToDtoInfoList(specialCases.getContent());

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
        return BaseService.optimizedSearch(specialCaseRepository, specialCaseMapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_SPECIAL_CASE')")
    public SpecialCaseDTO.Info create(SpecialCaseDTO.Create dto) {
        if (dto.getAssessorNationalCode().equals(dto.getAssessNationalCode())) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "assessNationalCode and assessorNationalCode",
                    messageSource.getMessage("exception.nationalCode.duplicate", null, locale));
        }
        List<SpecialCaseDTO.Info> byAssessNationalCodeAndStatusCode = getByAssessNationalCodeAndStatusCode(dto.getAssessNationalCode(), SPECIAL_ACTIVE);

        if (!byAssessNationalCodeAndStatusCode.isEmpty())
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "assessNationalCode and active status is exist",
                    messageSource.getMessage("exception.nationalCode.active.status.duplicate", null, LocaleContextHolder.getLocale()));

        SpecialCase specialCase = specialCaseMapper.dtoCreateToEntity(dto);
        try {
            specialCase.setStatusCatalogId(catalogService.getByCode(SPECIAL_INITIAL_REGISTRATION).getId());
            specialCase = specialCaseRepository.save(specialCase);
            return specialCaseMapper.entityToDtoInfo(specialCase);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_SPECIAL_CASE')")
    public SpecialCaseDTO.Info update(Long id, SpecialCaseDTO.Update dto) {
        if (dto.getAssessorNationalCode().equals(dto.getAssessNationalCode())) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "assessNationalCode and assessorNationalCode",
                    messageSource.getMessage("exception.nationalCode.duplicate", null, locale));
        }
        List<SpecialCaseDTO.Info> byAssessNationalCodeAndStatusCode = getByAssessNationalCodeAndStatusCodeNotIn
                (dto.getAssessNationalCode(), SPECIAL_ACTIVE, Collections.singletonList(id));

        if (!byAssessNationalCodeAndStatusCode.isEmpty())
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "assessNationalCode and active status is exist",
                    messageSource.getMessage("exception.nationalCode.active.status.duplicate", null, LocaleContextHolder.getLocale()));

        SpecialCase specialcase = specialCaseRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        specialCaseMapper.update(specialcase, dto);
        try {
            SpecialCase save = specialCaseRepository.save(specialcase);
            return specialCaseMapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_SPECIAL_CASE')")
    public void delete(Long id) {
        SpecialCase specialcase = specialCaseRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        specialCaseRepository.delete(specialcase);
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
        List<Long> expireIds = specialCaseRepository.findAllExpireSpecialCase(DateUtil.todayDate(), revokedStatusId).stream().map(SpecialCase::getId).toList();
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

            Optional<SpecialCase> optionalSpecialCase = specialCaseRepository.findById(id);
            if (optionalSpecialCase.isPresent()) {
                SpecialCase specialCase = optionalSpecialCase.get();

                if (Objects.nonNull(specialCase.getStatusCatalog().getCode()) && specialCase.getStatusCatalog().getCode().equals(SPECIAL_INITIAL_REGISTRATION)) {
                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode(SPECIAL_ACTIVE);
                    optionalCatalog.ifPresent(catalog -> specialCase.setStatusCatalogId(catalog.getId()));
                } else if (Objects.nonNull(specialCase.getStatusCatalog().getCode()) && specialCase.getStatusCatalog().getCode().equals(SPECIAL_ACTIVE)) {
                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode(SPECIAL_REVOKED);
                    optionalCatalog.ifPresent(catalog -> specialCase.setStatusCatalogId(catalog.getId()));
                }
                specialCaseRepository.save(specialCase);
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
    
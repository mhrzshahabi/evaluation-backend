
package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ISpecialCaseService;
import com.nicico.evaluation.mapper.SpecialCaseMapper;
import com.nicico.evaluation.model.SpecialCase;
import com.nicico.evaluation.repository.SpecialCaseRepository;
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

@RequiredArgsConstructor
@Service
public class SpecialCaseService implements ISpecialCaseService {

    private final SpecialCaseRepository specialCaseRepository;
    private final SpecialCaseMapper specialCaseMapper;
    private final PageableMapper pageableMapper;
    private final ResourceBundleMessageSource messageSource;

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
        if(dto.getAssessorNationalCode().equals(dto.getAssessNationalCode())) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "assessNationalCode and assessorNationalCode",
                    messageSource.getMessage("exception.nationalCode.duplicate", null, locale));
        }
        SpecialCase specialCase = specialCaseMapper.dtoCreateToEntity(dto);
        try {
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
        if(dto.getAssessorNationalCode().equals(dto.getAssessNationalCode())) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave,
                    "assessNationalCode and assessorNationalCode",
                    messageSource.getMessage("exception.nationalCode.duplicate", null, locale));
        }
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
}
    
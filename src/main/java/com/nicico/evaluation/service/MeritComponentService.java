package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.KPITypeDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IKPITypeService;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.iservice.IMeritComponentTypeService;
import com.nicico.evaluation.mapper.MeritComponentMapper;
import com.nicico.evaluation.model.MeritComponent;
import com.nicico.evaluation.repository.MeritComponentRepository;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MeritComponentService implements IMeritComponentService {

    private final MeritComponentMapper mapper;
    private final PageableMapper pageableMapper;
    private final MeritComponentRepository repository;
    private final IMeritComponentTypeService meritComponentTypeService;
    private final IKPITypeService typeService;
    private final ResourceBundleMessageSource messageSource;


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public MeritComponentDTO.Info get(Long id) {
        MeritComponent meritComponent = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(meritComponent);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public MeritComponentDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<MeritComponent> meritComponents = repository.findAll(pageable);
        List<MeritComponentDTO.Info> meritComponentInfos = mapper.entityToDtoInfoList(meritComponents.getContent());

        MeritComponentDTO.Response response = new MeritComponentDTO.Response();
        MeritComponentDTO.SpecResponse specResponse = new MeritComponentDTO.SpecResponse();

        if (meritComponentInfos != null) {
            response.setData(meritComponentInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) meritComponents.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_MERIT_COMPONENT')")
    public MeritComponentDTO.Info create(MeritComponentDTO.Create dto) {
        MeritComponent meritComponent = mapper.dtoCreateToEntity(dto);
        try {
            MeritComponent meritComponentAdd = repository.save(meritComponent);
            createAllMeritComponentType(dto.getKpiTypeId(), meritComponentAdd.getId());
            return mapper.entityToDtoInfo(meritComponentAdd);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    public BaseResponse batchCreate(MeritComponentDTO.BatchCreate dto) {
        BaseResponse response = new BaseResponse();
        try {
            KPITypeDTO.Info kpiType = typeService.getByCode(dto.getKpiTypeCode());
            if (Objects.nonNull(kpiType)) {
                MeritComponentDTO.Create createDto = new MeritComponentDTO.Create();
                createDto.setKpiTypeId(Collections.singletonList(kpiType.getId()));
                createDto.setCode(dto.getCode());
                createDto.setTitle(dto.getTitle());
                create(createDto);
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setMessage(messageSource.getMessage("exception.not.exist.kpi-type", new Object[]{dto.getCode()}, LocaleContextHolder.getLocale()));
            }
        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage(exception.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_MERIT_COMPONENT')")
    public MeritComponentDTO.Info update(Long id, MeritComponentDTO.Update dto) {
        MeritComponent meritComponent = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(meritComponent, dto);
        try {
            List<Long> meritComponentTypeIds = meritComponentTypeService.findAllByMeritComponentId(meritComponent.getId()).stream().map(MeritComponentTypeDTO.Info::getId).toList();
            if (!meritComponentTypeIds.isEmpty())
                meritComponentTypeService.deleteAll(meritComponentTypeIds);
            createAllMeritComponentType(dto.getKpiTypeId(), id);
            MeritComponent save = repository.save(meritComponent);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    private void createAllMeritComponentType(List<Long> kpiTypeId, Long id) {

        List<MeritComponentTypeDTO.Create> createListDto = new ArrayList<>();
        kpiTypeId.forEach(typeId -> {
            MeritComponentTypeDTO.Create meritComponentTypeDTO = new MeritComponentTypeDTO.Create();
            meritComponentTypeDTO.setMeritComponentId(id);
            meritComponentTypeDTO.setKpiTypeId(typeId);
            createListDto.add(meritComponentTypeDTO);
        });
        meritComponentTypeService.createAll(createListDto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_MERIT_COMPONENT')")
    public void delete(Long id) {
        MeritComponent meritComponent = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(meritComponent);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public SearchDTO.SearchRs<MeritComponentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    public MeritComponentDTO.Info getByCode(String code) {
        MeritComponent meritComponent = repository.findFirstByCode(code).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(meritComponent);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست مولفه شایستگی");
        return new ExcelGenerator.ExcelDownload(body);
    }

}

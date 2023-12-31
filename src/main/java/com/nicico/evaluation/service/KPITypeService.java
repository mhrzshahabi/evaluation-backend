package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.KPITypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IKPITypeService;
import com.nicico.evaluation.mapper.KPITypeMapper;
import com.nicico.evaluation.model.KPIType;
import com.nicico.evaluation.repository.KPITypeRepository;
import com.nicico.evaluation.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KPITypeService implements IKPITypeService {

    private final KPITypeMapper mapper;
    private final KPITypeRepository repository;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_KPI_TYPE')")
    public KPITypeDTO.Info get(Long id) {
        KPIType kpiType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(kpiType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_KPI_TYPE')")
    public KPITypeDTO.Info getByCode(String code) {
        KPIType kpiType = repository.findAllByCode(code);
        return mapper.entityToDtoInfo(kpiType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_KPI_TYPE')")
    public List<KPITypeDTO.Info> findAll() {
        List<KPIType> kpiType = repository.findAll();
        return mapper.entityToDtoInfoList(kpiType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_KPI_TYPE')")
    public KPITypeDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<KPIType> kpiTypes = repository.findAll(pageable);
        List<KPITypeDTO.Info> kpiTypeInfos = mapper.entityToDtoInfoList(kpiTypes.getContent());

        KPITypeDTO.Response response = new KPITypeDTO.Response();
        KPITypeDTO.SpecResponse specResponse = new KPITypeDTO.SpecResponse();

        if (kpiTypeInfos != null) {
            response.setData(kpiTypeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) kpiTypes.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_KPI_TYPE')")
    public SearchDTO.SearchRs<KPITypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);

    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_KPI_TYPE')")
    public KPITypeDTO.Info create(KPITypeDTO.Create dto) {
        KPIType kpiType = mapper.dtoCreateToEntity(dto);
        try {
            KPIType save = repository.save(kpiType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('C_KPI_TYPE')")
    public BaseResponse batchCreate(KPITypeDTO.Create dto) {
        BaseResponse response = new BaseResponse();
        try {
            KPIType kpiType = mapper.dtoCreateToEntity(dto);
            repository.save(kpiType);
            response.setStatus(HttpStatus.OK.value());
        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage(exception.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_KPI_TYPE')")
    public KPITypeDTO.Info update(Long id, KPITypeDTO.Update dto) {
        KPIType kPIType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(kPIType, dto);
        try {
            KPIType save = repository.save(kPIType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_KPI_TYPE')")
    public void delete(Long id) {
        KPIType kPIType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(kPIType);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

}

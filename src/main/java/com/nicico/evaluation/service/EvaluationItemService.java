package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationItemService;
import com.nicico.evaluation.mapper.EvaluationItemMapper;
import com.nicico.evaluation.model.EvaluationItem;
import com.nicico.evaluation.model.KPIType;
import com.nicico.evaluation.repository.EvaluationItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EvaluationItemService implements IEvaluationItemService {

    private final EvaluationItemMapper mapper;
    private final EvaluationItemRepository repository;
    private final PageableMapper pageableMapper;


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public EvaluationItemDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<EvaluationItem> groups = repository.findAll(pageable);
        List<EvaluationItemDTO.Info> groupInfos = mapper.entityToDtoInfoList(groups.getContent());

        EvaluationItemDTO.Response response = new EvaluationItemDTO.Response();
        EvaluationItemDTO.SpecResponse specResponse = new EvaluationItemDTO.SpecResponse();

        if (groupInfos != null) {
            response.setData(groupInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) groups.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public EvaluationItemDTO.Info get(Long id) {
        EvaluationItem group = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP')")
    public EvaluationItemDTO.Info create(EvaluationItemDTO.Create dto) {
        EvaluationItem group = repository.save(mapper.dtoCreateToEntity(dto));
        return mapper.entityToDtoInfo(group);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP')")
    public EvaluationItemDTO.Info update(Long id, EvaluationItemDTO.Update dto) {
        EvaluationItem evaluationItem = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(evaluationItem, dto);
        try {
            EvaluationItem save = repository.save(evaluationItem);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP')")
    public void delete(Long id) {
        EvaluationItem group = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(group);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP')")
    public SearchDTO.SearchRs<EvaluationItemDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }
}

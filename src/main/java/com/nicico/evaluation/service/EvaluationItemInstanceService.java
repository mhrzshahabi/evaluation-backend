package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.EvaluationItemInstanceDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationItemInstanceService;
import com.nicico.evaluation.mapper.EvaluationItemInstanceMapper;
import com.nicico.evaluation.model.EvaluationItem;
import com.nicico.evaluation.model.EvaluationItemInstance;
import com.nicico.evaluation.repository.EvaluationItemInstanceRepository;
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
public class EvaluationItemInstanceService implements IEvaluationItemInstanceService {

    private final EvaluationItemInstanceMapper mapper;
    private final EvaluationItemInstanceRepository repository;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM_INSTANCE')")
    public EvaluationItemInstanceDTO.Info get(Long id) {
        EvaluationItemInstance evaluationItemInstance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(evaluationItemInstance);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM_INSTANCE')")
    public List<EvaluationItemInstanceDTO.Info> getAllByEvaluationItemId(List<Long> evaluationItemIds) {
        List<EvaluationItemInstance> allByEvaluationItemId = repository.getAllByEvaluationItemIdIn(evaluationItemIds);
        return mapper.entityToDtoInfoList(allByEvaluationItemId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM_INSTANCE')")
    public EvaluationItemInstanceDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<EvaluationItemInstance> evaluationItemInstances = repository.findAll(pageable);
        List<EvaluationItemInstanceDTO.Info> evaluationItemInstanceInfos = mapper.entityToDtoInfoList(evaluationItemInstances.getContent());

        EvaluationItemInstanceDTO.Response response = new EvaluationItemInstanceDTO.Response();
        EvaluationItemInstanceDTO.SpecResponse specResponse = new EvaluationItemInstanceDTO.SpecResponse();

        if (evaluationItemInstanceInfos != null) {
            response.setData(evaluationItemInstanceInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) evaluationItemInstances.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM_INSTANCE')")
    public SearchDTO.SearchRs<EvaluationItemInstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);

    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_ITEM_INSTANCE')")
    public EvaluationItemInstanceDTO.Info create(EvaluationItemInstanceDTO.Create dto) {
        EvaluationItemInstance evaluationItemInstance = mapper.dtoCreateToEntity(dto);
        try {
            EvaluationItemInstance save = repository.save(evaluationItemInstance);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_ITEM_INSTANCE')")
    public List<EvaluationItemInstanceDTO.Info> createAll(List<EvaluationItemInstanceDTO.Create> requests) {
        return requests.stream().map(this::create).toList();
    }

    @Override
    @PreAuthorize("hasAuthority('C_EVALUATION_ITEM_INSTANCE')")
    public BaseResponse batchCreate(EvaluationItemInstanceDTO.Create dto) {
        BaseResponse response = new BaseResponse();
        try {
            EvaluationItemInstance evaluationItemInstance = mapper.dtoCreateToEntity(dto);
            repository.save(evaluationItemInstance);
            response.setStatus(HttpStatus.OK.value());
        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage(exception.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION_ITEM_INSTANCE')")
    public EvaluationItemInstanceDTO.Info update(Long id, EvaluationItemInstanceDTO.Update dto) {
        EvaluationItemInstance evaluationItemInstance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(evaluationItemInstance, dto);
        try {
            EvaluationItemInstance save = repository.save(evaluationItemInstance);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION_ITEM_INSTANCE')")
    public void delete(Long id) {
        EvaluationItemInstance evaluationItemInstance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(evaluationItemInstance);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION_ITEM_INSTANCE')")
    public void deleteAll(List<Long> ids) {
        ids.forEach(this::delete);
    }
}

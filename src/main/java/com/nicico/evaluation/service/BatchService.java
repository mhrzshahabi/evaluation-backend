package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IBatchDetailService;
import com.nicico.evaluation.iservice.IBatchService;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.mapper.BatchMapper;
import com.nicico.evaluation.model.Batch;
import com.nicico.evaluation.repository.BatchRepository;
import com.nicico.evaluation.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BatchService implements IBatchService {

    private final BatchMapper mapper;
    private final BatchRepository repository;
    private final PageableMapper pageableMapper;
    private final ICatalogService catalogService;
    private final IBatchDetailService batchDetailService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH')")
    public BatchDTO.Info get(Long id) {
        Batch batch = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(batch);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH')")
    public BatchDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Batch> batches = repository.findAll(pageable);
        List<BatchDTO.Info> batchInfos = mapper.entityToDtoInfoList(batches.getContent());

        BatchDTO.Response response = new BatchDTO.Response();
        BatchDTO.SpecResponse specResponse = new BatchDTO.SpecResponse();

        if (batchInfos != null) {
            response.setData(batchInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) batches.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH')")
    public SearchDTO.SearchRs<BatchDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @PreAuthorize("hasAuthority('C_BATCH')")
    public BaseResponse create(BatchDTO.Create dto) {
        try {
            Batch batch = mapper.dtoCreateToEntity(dto);
            batch.setStatusCatalogId(catalogService.getByCode("In-progress").getId());
            Batch save = repository.save(batch);
            BatchDetailDTO.CreateList detailCreate = new BatchDetailDTO.CreateList();
            CatalogDTO.Info catalogDTO = catalogService.get(dto.getTitleCatalogId());
            detailCreate.setInputDetails(dto.getInputDetails());
            detailCreate.setServiceType(catalogDTO.getCode());
            detailCreate.setBatchId(save.getId());
            return batchDetailService.create(detailCreate);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_BATCH')")
    public BatchDTO.Info update(Long id, BatchDTO.Update dto) {
        Batch batch = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(batch, dto);
        try {
            Batch save = repository.save(batch);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_BATCH')")
    public void delete(Long id) {
        Batch batch = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(batch);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

}

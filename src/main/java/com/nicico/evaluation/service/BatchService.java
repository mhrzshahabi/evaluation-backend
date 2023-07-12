package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IAttachmentService;
import com.nicico.evaluation.iservice.IBatchDetailService;
import com.nicico.evaluation.iservice.IBatchService;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.mapper.BatchDetailMapper;
import com.nicico.evaluation.mapper.BatchMapper;
import com.nicico.evaluation.model.Batch;
import com.nicico.evaluation.repository.BatchDetailRepository;
import com.nicico.evaluation.repository.BatchRepository;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.EvaluationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BatchService implements IBatchService {

    private final BatchMapper mapper;
    private final BatchRepository repository;
    private final PageableMapper pageableMapper;
    private final ICatalogService catalogService;
    private final BatchDetailMapper batchDetailMapper;
    private final IAttachmentService attachmentService;
    private final IBatchDetailService batchDetailService;
    private final BatchDetailRepository batchDetailRepository;

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
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH_DETAIL')")
    public SearchDTO.SearchRs<BatchDetailDTO.Info> batchDetailSearch(SearchDTO.SearchRq request, Long batchId) throws NoSuchFieldException, IllegalAccessException {

        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq batchIdCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.equals)
                .setFieldName("batchId")
                .setValue(batchId);

        criteriaRqList.add(batchIdCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        return BaseService.optimizedSearch(batchDetailRepository, batchDetailMapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @PreAuthorize("hasAuthority('C_BATCH')")
    public BaseResponse create(BatchDTO.Create dto) {
        try {
            Batch batch = mapper.dtoCreateToEntity(dto);
            batch.setStatusCatalogId(catalogService.getByCode("In-progress").getId());
            Batch save = repository.save(batch);

            if (Objects.nonNull(dto.getFmsKey())) {
                AttachmentDTO.Create attachmentCreate = mapper.dtoCreateToAttachmentDtoCreate(dto);
                attachmentCreate.setObjectType(EvaluationConstant.BATCH);
                attachmentCreate.setObjectId(save.getId());
                attachmentService.create(attachmentCreate);
            }

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
    @PreAuthorize("hasAuthority('U_BATCH')")
    public void updateStatus(Long id, Long statusCatalogId) {
        Optional<Batch> optionalBatch = repository.findById(id);
        if (optionalBatch.isPresent()) {
            Batch batch = optionalBatch.get();
            batch.setStatusCatalogId(statusCatalogId);
            repository.save(batch);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<WebSocketDTO> getForNotificationPanel() {
        List<Batch> batchList = repository.getNeededDataForWebSocket(catalogService.getByCode("In-progress").getId(), catalogService.getByCode("Completed").getId());
        return mapper.entityToWebSocketDtoList(batchList);
    }

}

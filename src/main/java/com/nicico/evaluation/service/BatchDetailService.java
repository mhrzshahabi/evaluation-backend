package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.dto.KPITypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IBatchDetailService;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IKPITypeService;
import com.nicico.evaluation.mapper.BatchDetailMapper;
import com.nicico.evaluation.model.BatchDetail;
import com.nicico.evaluation.repository.BatchDetailRepository;
import com.nicico.evaluation.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BatchDetailService implements IBatchDetailService {

    private final ModelMapper modelMapper;
    private final BatchDetailMapper mapper;
    private final PageableMapper pageableMapper;
    private final ICatalogService catalogService;
    private final IKPITypeService kpiTypeService;
    private final BatchDetailRepository repository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH_DETAIL')")
    public BatchDetailDTO.Info get(Long id) {
        BatchDetail batchDetail = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
//        return mapper.entityToDtoInfo(batchDetail);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH_DETAIL')")
    public BatchDetailDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<BatchDetail> batchDetails = repository.findAll(pageable);
//        List<BatchDetailDTO.Info> batchDetailInfos = mapper.entityToDtoInfoList(batchDetails.getContent());
        List<BatchDetailDTO.Info> batchDetailInfos = new ArrayList<>();

        BatchDetailDTO.Response response = new BatchDetailDTO.Response();
        BatchDetailDTO.SpecResponse specResponse = new BatchDetailDTO.SpecResponse();

        if (batchDetailInfos != null) {
            response.setData(batchDetailInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) batchDetails.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH_DETAIL')")
    public SearchDTO.SearchRs<BatchDetailDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
//        return BaseService.optimizedSearch(repository,  mapper::entityToDtoInfo, request);
        return null;
    }

    @Override
    @Transactional
//    @PreAuthorize("hasAuthority('C_BATCH_DETAIL')")
    public BatchDetailDTO.Info create(BatchDetailDTO.CreateList dto) {
        try {
            dto.getInputDetails().forEach(detail -> {
                BatchDetailDTO.Create create = new BatchDetailDTO.Create();
                create.setInputDTO(detail.toString());
                create.setBatchId(dto.getBatchId());
                create.setStatusCatalogId(catalogService.getByCode("Processing").getId());
                BatchDetail batchDetail = mapper.dtoCreateToEntity(create);
                repository.save(batchDetail);
            });
            batchCreate(dto);
            return null;
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_BATCH_DETAIL')")
    public BatchDetailDTO.Info update(BatchDetailDTO.Update dto) {
        BatchDetail batchDetail = repository.findById(dto.getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
//        mapper.update(batchDetail, dto);
        try {
            BatchDetail save = repository.save(batchDetail);
//            return mapper.entityToDtoInfo(save);
            return null;
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_BATCH_DETAIL')")
    public void updateStatus(String statusCode) {
        Long statId = catalogService.getByCode(statusCode).getId();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_BATCH_DETAIL')")
    public void delete(Long id) {
        BatchDetail batchDetail = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(batchDetail);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    public List<BatchDetailDTO.Info> getBatchDetailListByBatchId(Long batchId) {
        List<BatchDetail> batchDetailList = repository.findAllByBatchId(batchId);
//        return mapper.entityToDtoInfoList(batchDetailList);
        return null;
    }

    @Async
    void batchCreate(BatchDetailDTO.CreateList dto) {

        String success = "Successful";
        String fail = "Failed";
        List<BatchDetail> batchDetailList = repository.findAllByBatchId(dto.getBatchId());
        switch (dto.getServiceType()) {
            case "BatchCreate-KPIType-Excel" -> {
                batchDetailList.forEach(item -> {
                    BaseResponse response = kpiTypeService.batchCreate(modelMapper.map(item.getInputDTO(), KPITypeDTO.Create.class));
                    if (response.getStatus() == 200)
                        updateStatus(success);
                    else
                        updateStatus(fail);
                });
            }
        }
    }

}

package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IBatchDetailService;
import com.nicico.evaluation.mapper.BatchDetailMapper;
import com.nicico.evaluation.model.BatchDetail;
import com.nicico.evaluation.repository.BatchDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BatchDetailService implements IBatchDetailService {

    private final BatchDetailMapper mapper;
    private final PageableMapper pageableMapper;
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
    @PreAuthorize("hasAuthority('C_BATCH_DETAIL')")
    public BatchDetailDTO.Info create(BatchDetailDTO.Create dto) {
//        BatchDetail batchDetail = mapper.dtoCreateToEntity(dto);
        BatchDetail batchDetail = new BatchDetail();
        try {
            BatchDetail save = repository.save(batchDetail);
//            return mapper.entityToDtoInfo(save);
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

}

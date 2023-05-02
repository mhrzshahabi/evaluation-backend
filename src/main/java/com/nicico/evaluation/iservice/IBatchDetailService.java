package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.model.BatchDetail;
import com.nicico.evaluation.utility.BaseResponse;

import java.util.List;

public interface IBatchDetailService {

    BatchDetailDTO.Info get(Long id);

    BatchDetailDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<BatchDetailDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    BaseResponse create(BatchDetailDTO.CreateList dto);

    BatchDetailDTO.Info update(BatchDetailDTO.Update dto);

    void updateStatusAndExceptionTitle(Long id, Long statusCatalogId, String exceptionTitle);

    void delete(Long id);

    List<BatchDetail> findAllBatchDetailListByBatchId(Long batchId);

}

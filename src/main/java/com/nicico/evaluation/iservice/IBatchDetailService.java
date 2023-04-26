package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;

import java.util.List;

public interface IBatchDetailService {

    BatchDetailDTO.Info get(Long id);

    BatchDetailDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<BatchDetailDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    BatchDetailDTO.Info create(BatchDetailDTO.CreateList dto);

    BatchDetailDTO.Info update(BatchDetailDTO.Update dto);

    void updateStatus(String statsCode);

    void delete(Long id);

    List<BatchDetailDTO.Info> getBatchDetailListByBatchId(Long batchId);

}

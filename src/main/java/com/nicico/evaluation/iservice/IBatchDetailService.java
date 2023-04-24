package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;

import java.util.List;

public interface IBatchDetailService {

    BatchDetailDTO.Info get(Long id);

    BatchDetailDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<BatchDetailDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    BatchDetailDTO.Info create(BatchDetailDTO.Create dto);

    BatchDetailDTO.Info update(BatchDetailDTO.Update dto);

    void delete(Long id);

    List<BatchDetailDTO.Info> getBatchDetailListByBatchId(Long batchId);

}

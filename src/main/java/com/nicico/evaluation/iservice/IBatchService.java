package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.utility.BaseResponse;

import java.util.List;

public interface IBatchService {

    BatchDTO.Info get(Long id);

    List<BatchDetailDTO.Info> getDetailByBatchId(Long batchId);

    BatchDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<BatchDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    BaseResponse create(BatchDTO.Create dto);

}

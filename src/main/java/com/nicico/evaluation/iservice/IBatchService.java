package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.utility.BaseResponse;

public interface IBatchService {

    BatchDTO.Info get(Long id);

    BatchDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<BatchDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<BatchDetailDTO.Info> batchDetailSearch(SearchDTO.SearchRq request, Long batchId) throws NoSuchFieldException, IllegalAccessException;

    BaseResponse create(BatchDTO.Create dto);

}

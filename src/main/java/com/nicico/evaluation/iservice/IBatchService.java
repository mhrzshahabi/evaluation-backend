package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.utility.BaseResponse;

public interface IBatchService {

    BatchDTO.Info get(Long id);

    BatchDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<BatchDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    BaseResponse create(BatchDTO.Create dto);

    BatchDTO.Info update(BatchDTO.Update dto);

    void delete(Long id);

}

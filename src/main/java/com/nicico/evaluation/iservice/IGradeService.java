package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GradeDTO;

public interface IGradeService {

    GradeDTO.Info get(Long id) throws Exception;

    GradeDTO.Info getByCode(String code);

    GradeDTO.SpecResponse list(int count, int startIndex);

    TotalResponse<GradeDTO.Info> search(NICICOCriteria request);

}

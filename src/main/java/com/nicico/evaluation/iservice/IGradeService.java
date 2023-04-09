package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GradeDTO;

import java.util.List;

public interface IGradeService {

    GradeDTO.Info get(Long id) throws Exception;

    GradeDTO.Info getByCode(String code);

    List<GradeDTO.Info> list();

    TotalResponse<GradeDTO.Info> search(NICICOCriteria request);

}

package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.GradeDTO;

import java.util.List;

public interface IGradeService {

    GradeDTO.Info get(Long id);

    List<GradeDTO.Info> getAllByCodeIn(List<String> codes);

    GradeDTO.SpecResponse list(int count, int startIndex);

    TotalResponse<GradeDTO.Info> search(NICICOCriteria request);

    SearchDTO.SearchRs<GradeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;


}

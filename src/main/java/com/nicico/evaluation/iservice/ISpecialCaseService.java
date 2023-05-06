
package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.SpecialCaseDTO;

public interface ISpecialCaseService {

    SpecialCaseDTO.Info get(Long id);

    SpecialCaseDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<SpecialCaseDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SpecialCaseDTO.Info create(SpecialCaseDTO.Create dto);

    SpecialCaseDTO.Info update(Long id, SpecialCaseDTO.Update dto);

    void delete(Long id);
}
    
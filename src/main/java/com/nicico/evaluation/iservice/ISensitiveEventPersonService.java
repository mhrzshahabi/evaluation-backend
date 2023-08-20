package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.SensitiveEventPersonDTO;

public interface ISensitiveEventPersonService {

    SensitiveEventPersonDTO.Info get(Long id);

    SensitiveEventPersonDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<SensitiveEventPersonDTO.LastActiveMeritInfo> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SensitiveEventPersonDTO.Info create(SensitiveEventPersonDTO.Create dto);

    SensitiveEventPersonDTO.Info update(Long id, SensitiveEventPersonDTO.Update dto);

    void delete(Long id);

}

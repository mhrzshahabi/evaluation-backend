package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.SensitiveEventsDTO;

public interface ISensitiveEventsService {

    SensitiveEventsDTO.Info get(Long id);

    SensitiveEventsDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<SensitiveEventsDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SensitiveEventsDTO.Info create(SensitiveEventsDTO.Create dto);

    SensitiveEventsDTO.Info update(SensitiveEventsDTO.Update dto);

    void delete(Long id);

}

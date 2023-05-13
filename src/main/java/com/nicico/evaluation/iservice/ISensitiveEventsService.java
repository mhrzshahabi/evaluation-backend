package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.SensitiveEventsDTO;

import java.util.List;

public interface ISensitiveEventsService {

    SensitiveEventsDTO.Info get(Long id);

    List<SensitiveEventsDTO.Info> getAllByNationalCode(String nationalCode);

    SensitiveEventsDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<SensitiveEventsDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SensitiveEventsDTO.Info create(SensitiveEventsDTO.Create dto);

    SensitiveEventsDTO.Info update(Long id, SensitiveEventsDTO.Update dto);

    void delete(Long id);

}

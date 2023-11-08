package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.SensitiveEventsDTO;

public interface ISensitiveEventPersonViewService {

    SearchDTO.SearchRs<SensitiveEventsDTO.SensitiveEventPersonInfo> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;
}

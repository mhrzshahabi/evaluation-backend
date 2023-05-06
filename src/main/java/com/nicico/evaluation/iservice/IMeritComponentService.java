package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;

public interface IMeritComponentService {

    MeritComponentDTO.Info get(Long id);

    MeritComponentDTO.SpecResponse list(int count, int startIndex);

    MeritComponentDTO.Info create(MeritComponentDTO.Create dto);

    MeritComponentDTO.Info update(Long id, MeritComponentDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<MeritComponentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    MeritComponentDTO.Info getByCode(String code);

}

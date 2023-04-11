package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;

public interface IMeritComponentTypeService {

    MeritComponentTypeDTO.Info get(Long id);

    MeritComponentTypeDTO.SpecResponse list(int count, int startIndex);

    MeritComponentTypeDTO.Info create(MeritComponentTypeDTO.Create dto);

    MeritComponentTypeDTO.Info update(MeritComponentTypeDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<MeritComponentTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}

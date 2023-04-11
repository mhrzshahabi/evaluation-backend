package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.KPITypeDTO;

public interface IKPITypeService {

    KPITypeDTO.Info get(Long id);

    KPITypeDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<KPITypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    KPITypeDTO.Info create(KPITypeDTO.Create dto);

    KPITypeDTO.Info update(KPITypeDTO.Update dto);

    void delete(Long id);

}

package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.InstanceDTO;

public interface IInstanceService {

    InstanceDTO.SpecResponse list(int count, int startIndex);

    InstanceDTO.Info get(Long id);

    InstanceDTO.Info create(InstanceDTO.Create dto);

    InstanceDTO.Info update(InstanceDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<InstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}

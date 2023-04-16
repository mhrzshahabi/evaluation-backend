package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;

import java.util.List;

public interface IInstanceGroupTypeMeritService {

    InstanceGroupTypeMeritDTO.Info get(Long id);

    List<InstanceGroupTypeMeritDTO.InstanceInfo> getAllInstanceByGroupTypeMeritId(Long id);

    InstanceGroupTypeMeritDTO.SpecResponse list(int count, int startIndex);

    TotalResponse<InstanceGroupTypeMeritDTO.Info> search(NICICOCriteria request);

    InstanceGroupTypeMeritDTO.Info create(InstanceGroupTypeMeritDTO.Create dto);

    InstanceGroupTypeMeritDTO.Info update(InstanceGroupTypeMeritDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<InstanceGroupTypeMeritDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;


}

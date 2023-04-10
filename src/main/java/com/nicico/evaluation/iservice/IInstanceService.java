package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.InstanceDTO;

public interface IInstanceService {

    InstanceDTO.SpecResponse list(int count, int startIndex);

    InstanceDTO.Info get(Long id);

    TotalResponse<InstanceDTO.Info> search(NICICOCriteria request);

    InstanceDTO.Info create(InstanceDTO.Create dto);

    InstanceDTO.Info update(InstanceDTO.Update dto);

    void delete(Long id);

}

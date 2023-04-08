package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.InstanceDTO;

import java.util.List;

public interface IInstanceService {
    List<InstanceDTO.Info> list();
    InstanceDTO.Info get(Long id);
    TotalResponse<InstanceDTO.Info> search(NICICOCriteria request);
    InstanceDTO.Info create(InstanceDTO.Create dto);
    InstanceDTO.Info update(InstanceDTO.Update dto);
    void delete(Long id);
}

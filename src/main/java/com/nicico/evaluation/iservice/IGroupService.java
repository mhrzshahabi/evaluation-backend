package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.model.Group;

import java.util.List;


public interface IGroupService {

    List<GroupDTO.Info> list();
    GroupDTO.Info get(Long id);
    TotalResponse<GroupDTO.Info> search(NICICOCriteria request);
    GroupDTO.Info create(GroupDTO.Create dto);
    GroupDTO.Info update(GroupDTO.Update dto);
    void delete(Long id);
}


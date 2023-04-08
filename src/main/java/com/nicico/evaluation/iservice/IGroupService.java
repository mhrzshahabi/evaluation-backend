package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.common.PageDTO;
import com.nicico.evaluation.dto.GroupDTO;
import org.springframework.data.domain.Pageable;


public interface IGroupService {

    PageDTO list(Pageable page);
    GroupDTO.Info get(Long id);
    TotalResponse<GroupDTO.Info> search(NICICOCriteria request);
    GroupDTO.Info create(GroupDTO.Create dto);
    GroupDTO.Info update(GroupDTO.Update dto);
    void delete(Long id);
}


package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupTypeDTO;

import java.util.List;

public interface IGroupTypeService {

    GroupTypeDTO.Info get(Long id);

    List<GroupTypeDTO.Info> list();

    TotalResponse<GroupTypeDTO.Info> search(NICICOCriteria request);

    GroupTypeDTO.Info create(GroupTypeDTO.Create dto);

    GroupTypeDTO.Info update(GroupTypeDTO.Update dto);

    void delete(Long id);
}

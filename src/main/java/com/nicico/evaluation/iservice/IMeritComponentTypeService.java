package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;

public interface IMeritComponentTypeService {

    MeritComponentTypeDTO.Info get(Long id);

    MeritComponentTypeDTO.SpecResponse list(int count, int startIndex);

    TotalResponse<MeritComponentTypeDTO.Info> search(NICICOCriteria request);

    MeritComponentTypeDTO.Info create(MeritComponentTypeDTO.Create dto);

    MeritComponentTypeDTO.Info update(MeritComponentTypeDTO.Update dto);

    void delete(Long id);

}

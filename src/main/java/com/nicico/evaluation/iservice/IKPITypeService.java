package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.KPITypeDTO;

public interface IKPITypeService {

    KPITypeDTO.Info get(Long id);

    KPITypeDTO.SpecResponse list(int count, int startIndex);

    TotalResponse<KPITypeDTO.Info> search(NICICOCriteria request);

    KPITypeDTO.Info create(KPITypeDTO.Create dto);

    KPITypeDTO.Info update(KPITypeDTO.Update dto);

    void delete(Long id);

}

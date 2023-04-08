package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.KPITypeDTO;

import java.util.List;

public interface IKPITypeService {

    KPITypeDTO.Info get(Long id) ;

    List<KPITypeDTO.Info> list();

    TotalResponse<KPITypeDTO.Info> search(NICICOCriteria request);

    KPITypeDTO.Info create(KPITypeDTO.Create dto);

    KPITypeDTO.Info update(KPITypeDTO.Update dto);

    void delete(Long id);
}

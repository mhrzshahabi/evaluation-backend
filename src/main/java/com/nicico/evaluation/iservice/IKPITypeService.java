package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.KPITypeDTO;
import com.nicico.evaluation.utility.BaseResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IKPITypeService {

    KPITypeDTO.Info get(Long id);

    List<KPITypeDTO.Info> findAll();

    KPITypeDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<KPITypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    KPITypeDTO.Info create(KPITypeDTO.Create dto);

    BaseResponse batchCreate(KPITypeDTO.Create dto);

    KPITypeDTO.Info update(Long id, KPITypeDTO.Update dto);

    void delete(Long id);

}

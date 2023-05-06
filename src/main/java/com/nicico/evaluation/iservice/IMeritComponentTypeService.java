package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;

import java.util.List;

public interface IMeritComponentTypeService {

    MeritComponentTypeDTO.Info get(Long id);

    List<MeritComponentTypeDTO.Info> findAllByMeritComponentId(Long meritComponentId);

    MeritComponentTypeDTO.Info findFirstByMeritComponentId(Long meritComponentId);

    MeritComponentTypeDTO.SpecResponse list(int count, int startIndex);

    MeritComponentTypeDTO.Info create(MeritComponentTypeDTO.Create dto);

    List<MeritComponentTypeDTO.Info> createAll(List<MeritComponentTypeDTO.Create> requests);

    MeritComponentTypeDTO.Info update(Long id, MeritComponentTypeDTO.Update dto);

    void delete(Long id);

    void deleteAll(List<Long> ids);

    SearchDTO.SearchRs<MeritComponentTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}

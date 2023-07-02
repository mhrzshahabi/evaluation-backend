package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.utility.ExcelGenerator;

import java.util.List;

public interface IInstanceService {

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

    InstanceDTO.SpecResponse list(int count, int startIndex);

    InstanceDTO.Info get(Long id);

    InstanceDTO.Info create(InstanceDTO.Create dto);

    InstanceDTO.Info update(Long id, InstanceDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<InstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<InstanceDTO.Info> searchByPostMeritId(SearchDTO.SearchRq request, Long postMeritId) throws IllegalAccessException, NoSuchFieldException;

    InstanceDTO.Info getByCode(String code);
}

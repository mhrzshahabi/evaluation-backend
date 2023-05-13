package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.utility.ExcelGenerator;

import java.util.List;

import java.util.List;

public interface IGroupTypeMeritService {

    GroupTypeMeritDTO.Info get(Long id);

    List<GroupTypeMeritDTO.Info> getAllByGroupTypeId(Long groupTypeId);

    List<GroupTypeMeritDTO.Info> getTypeMeritInstanceByAssessPostCode(String assessPostCode);

    GroupTypeMeritDTO.SpecResponse list(int count, int startIndex);

    GroupTypeMeritDTO.Info create(GroupTypeMeritDTO.Create dto);

    GroupTypeMeritDTO.Info update(Long id, GroupTypeMeritDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<GroupTypeMeritDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

}

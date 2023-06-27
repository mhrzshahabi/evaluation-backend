package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupPostDTO;
import com.nicico.evaluation.utility.ExcelGenerator;

import java.util.List;

public interface IGroupPostService {

    GroupPostDTO.SpecResponse list(int count, int startIndex);

    GroupPostDTO.Info get(Long id);

    SearchDTO.SearchRs<GroupPostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    GroupPostDTO.Info getByCode(String code);

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

}


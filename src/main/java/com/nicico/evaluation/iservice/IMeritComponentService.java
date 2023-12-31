package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.ExcelGenerator;

import java.util.List;

public interface IMeritComponentService {

    MeritComponentDTO.Info get(Long id);

    MeritComponentDTO.Info findLastActiveByMeritComponentId(Long id);

    MeritComponentDTO.Info findByRevAndMeritComponentId(Long rev, Long id);

    MeritComponentDTO.SpecResponse list(int count, int startIndex);

    MeritComponentDTO.Info create(MeritComponentDTO.Create dto);

    BaseResponse batchCreate(MeritComponentDTO.BatchCreate dto);

    MeritComponentDTO.Info update(Long id, MeritComponentDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<MeritComponentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    MeritComponentDTO.Info getByCode(String code);

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

    MeritComponentDTO.Info changeStatus(Long id, MeritComponentDTO.ChangeStatus request);

    Long getMeritComponentStatusCatalogId(Long meritComponentId);

    Integer getNumberOfAdminWorkInWorkSpace();

    List<Long> getAdminWorkInWorkSpace();

    void updateMeritToAudit();
}

package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.utility.ExcelGenerator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IGroupTypeMeritService {

    GroupTypeMeritDTO.Info get(Long id);

    List<EvaluationItemDTO.MeritTupleDTO> getAllByGroupType(Long groupTypeId);

    List<GroupTypeMeritDTO.Info> getAllByGroupTypeIdAndMeritStatusId(Long groupTypeId, Long statusCatalogId);

    Long getTotalComponentWeightByGroupType(Long groupTypeId);

    GroupTypeMeritDTO.SpecResponse list(int count, int startIndex);

    GroupTypeMeritDTO.Info create(GroupTypeMeritDTO.Create dto);

    GroupTypeMeritDTO.Info update(Long id, GroupTypeMeritDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<GroupTypeMeritDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

}

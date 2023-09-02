package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.MeritComponentAuditDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.dto.SearchRequestDTO;
import com.nicico.evaluation.model.MeritComponentAudit;

import java.util.List;

public interface IMeritComponentAuditService {

    MeritComponentAudit findLastActiveByMeritComponentId(Long meritComponentId);

    MeritComponentAudit getPreviousById(Long meritComponentId);

    Integer getNumberOfExpertWorkInWorkSpace();

    List<Long> getExpertWorkInWorkSpace();

    MeritComponentAudit findAllByRevAndMeritComponentId(Long rev, Long meritComponentId);

    SearchDTO.SearchRs<MeritComponentDTO.Info> searchLastActiveMeritComponent(int startIndex, int count, SearchRequestDTO search);

    SearchDTO.SearchRs<MeritComponentDTO.Info> searchLastActiveMeritComponentKPIFilter(int startIndex, int count, SearchRequestDTO search);

    SearchDTO.SearchRs<MeritComponentAuditDTO.Info> getChangeList(SearchDTO.SearchRq request , Long id) throws IllegalAccessException, NoSuchFieldException;
}

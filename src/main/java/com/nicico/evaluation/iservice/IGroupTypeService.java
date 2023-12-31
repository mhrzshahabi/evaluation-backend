package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.GroupTypeByGroupByDTO;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.model.GroupType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IGroupTypeService {

    GroupTypeDTO.Info get(Long id);

    GroupTypeDTO.Info getByCode(String code);

    List<GroupTypeDTO.Info> getAllByPeriodId(Long periodId);

    List<GroupTypeDTO.Info> getAllByPostCodes(List<String> postCodes);

    List<GroupTypeDTO.Info> getAllByGroupId(Long groupId);

    List<GroupType> getTypeByAssessPostCode(List<String> assessPostCode, String levelDef);

    GroupTypeDTO.GroupTypeMaxWeight getWeightInfoByGroupId(Long groupId);

    GroupTypeDTO.SpecResponse list(int count, int startIndex);

    GroupTypeDTO.Info create(GroupTypeDTO.Create dto);

    GroupType getAllByGroupIdAndKpiTypeId(GroupType groupType);

    GroupTypeDTO.Info update(Long id, GroupTypeDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<GroupTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<GroupTypeByGroupByDTO.Info> searchByGroupBy1(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<GroupTypeDTO.GroupByInfo> searchByGroupBy(int count, int startIndex, SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<GroupTypeDTO.Info> searchByGroupId(Long groupId);
}

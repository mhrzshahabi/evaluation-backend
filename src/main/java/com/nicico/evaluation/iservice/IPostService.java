package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PostDTO;
import com.nicico.evaluation.model.Post;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface IPostService {

    PostDTO.Info get(Long id);

    Post getByPostCode(String postCode);

    PostDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<PostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    List<PostDTO.Info> getPostGradeHasNotGroupByPeriodId(Long periodId);

    List<PostDTO.Info> getPostGradeHasNotGroupByPostCodes(List<String> postCodes);

    List<PostDTO.Info> getGroupHasNotGroupTypeByPeriodId(Long periodId);

    List<PostDTO.Info> getGroupHasNotGroupTypeByPostCodes(List<String> postCodes);

    List<PostDTO.Info> getAllByGroupByPeriodId(Long periodId);
}

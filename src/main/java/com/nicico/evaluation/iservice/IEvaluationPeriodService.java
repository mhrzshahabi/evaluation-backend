package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.utility.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface IEvaluationPeriodService {

    EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod getWithPostInfo(Long id);

    EvaluationPeriodDTO.Info get(Long id);

    List<EvaluationPeriodDTO.Info> getAllByCreatorAndStartDateValidation(String creator, String toDayDate, Long statusId);

    List<EvaluationPeriodDTO.Info> getAllByAssessNationalCodeAndStatusCatalogId(String assessNationalCode, Long statusCatalogId, Long periodStatusId, int count, int startIndex);

    List<EvaluationPeriodDTO.RemainDate> getAllByCreatorAndRemainDateToEndDateValidation(String creator, String toDayDate, Long statusId);

    List<EvaluationPeriodDTO.Info> getAllByAssessorAndStartDateAssessment(String assessmentNationalCode, String toDayDate);

    List<EvaluationPeriodDTO.RemainDate> getAllByAssessorAndStartDateAssessmentAndStatusId(String assessmentNationalCode, String toDayDate, Long statusId);

    EvaluationPeriodDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<EvaluationPeriodDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    EvaluationPeriodDTO.Info create(EvaluationPeriodDTO.Create dto) throws ParseException;

    EvaluationPeriodDTO.Info update(Long id, EvaluationPeriodDTO.Update dto) throws ParseException;

    List<EvaluationPeriodPostDTO.Info> createEvaluationPeriodPost(Long id, Set<String> postCode);

    void delete(Long id);

    BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO);

    Boolean validatePosts(Long evaluationPeriodId);

    ResponseEntity<byte[]> downloadInvalidPostExcel(Long evaluationPeriodId) throws NoSuchFieldException, IllegalAccessException;

    String downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException, IOException;
}

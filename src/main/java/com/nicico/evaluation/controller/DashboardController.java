package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.dto.WorkSpaceDTO;
import com.nicico.evaluation.iservice.IWorkSpaceService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Api(value = "Dashboard")
@Slf4j
@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    private final IWorkSpaceService workSpaceService;

    /**
     * @return that contain list of WorkSpaceDTO.Info
     */
    @GetMapping(value = "/work-space/list")
    public ResponseEntity<List<WorkSpaceDTO.Info>> workSpaceList() {
        return new ResponseEntity<>(workSpaceService.workSpaceList(), HttpStatus.OK);
    }

    /**
     * @return that contain list of meritComponentIds
     */
    @GetMapping(value = "/work-space/detail")
    public ResponseEntity<List<Long>> workSpaceDetail(@RequestParam String code) {
        return new ResponseEntity<>(workSpaceService.workSpaceDetail(code), HttpStatus.OK);
    }

    /**
     * @return that contain list of WorkSpaceDTO.Info
     */
    @GetMapping(value = "/work-space/alarm")
    public ResponseEntity<List<WorkSpaceDTO.Info>> workSpaceAlarm(@RequestBody List<String> workSpaceCodeList) {
        return new ResponseEntity<>(workSpaceService.workSpaceAlarm(workSpaceCodeList), HttpStatus.OK);
    }

    /**
     * @return that contain list of evaluationPeriodList By User
     */
    @PostMapping(value = "/evaluation-period/list")
    public ResponseEntity<EvaluationDTO.SpecResponse> evaluationPeriodListByUser(@RequestParam int count, @RequestParam int startIndex) {
        SearchDTO.SearchRs<EvaluationPeriodDTO.Info> infoSearchRs = workSpaceService.evaluationPeriodListByUser(count, startIndex);
        final EvaluationDTO.Response response = new EvaluationDTO.Response();
        final EvaluationDTO.SpecResponse specRs = new EvaluationDTO.SpecResponse();
        response.setData(infoSearchRs.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + infoSearchRs.getList().size())
                .setTotalRows(infoSearchRs.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    // ارزیابی من

    /**
     * @return that result of my evaluation by evaluationPeriodId
     */
    @GetMapping(value = "/my-evaluation/{evaluationPeriodId}")
    public ResponseEntity<EvaluationDTO.EvaluationAverageScoreData> evaluationAverageScoreDataByUser(@PathVariable Long evaluationPeriodId, @RequestParam(value = "dashboardCategory", required = false)  String dashboardCategory) {
        return new ResponseEntity<>(workSpaceService.evaluationAverageScoreDataByUser(evaluationPeriodId, dashboardCategory), HttpStatus.OK);
    }

    // برترین امور مشارکت کننده

    /**
     * @return that result of most Participation Per Omoor by evaluationPeriodId
     */
    @GetMapping(value = "/most-participation/{evaluationPeriodId}")
    public ResponseEntity<List<EvaluationDTO.MostParticipationInFinalizedEvaluation>> mostParticipationPerOmoor(@PathVariable Long evaluationPeriodId) {
        return new ResponseEntity<>(workSpaceService.mostParticipationPerOmoor(evaluationPeriodId), HttpStatus.OK);
    }

    // میانگین ارزیابی امور من
    @GetMapping("/finalized-average-by-grade-and-period/{evaluationPeriodId}")
    public List<EvaluationDTO.AverageWeightDTO> getFinalizedAverageByGradeAndPeriodEvaluation(@PathVariable Long evaluationPeriodId) {
        return workSpaceService.getFinalizedAverageByGradeAndPeriodEvaluation(evaluationPeriodId);
    }

    // برترین ارزیابی شوندگان
    @GetMapping("/best-assess-by-omoor/{evaluationPeriodId}")
    public List<EvaluationDTO.BestAssessAverageScoreDTO> getBestAssessesByOmoor(@PathVariable Long evaluationPeriodId, @RequestParam int count, @RequestParam int startIndex, @RequestParam(value = "dashboardCategory", required = false) String dashboardCategory) {
        return workSpaceService.getBestAssessesByOmoor(count, startIndex, evaluationPeriodId, dashboardCategory);
    }

}

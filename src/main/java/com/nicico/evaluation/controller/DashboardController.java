package com.nicico.evaluation.controller;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.WorkSpaceDTO;
import com.nicico.evaluation.iservice.IWorkSpaceService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Api(value = "Dashboard")
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
    @GetMapping(value = "/evaluation-period/list")
    public ResponseEntity<List<EvaluationDTO.EvaluationPeriodDashboard>> evaluationPeriodListByUser() {
        return new ResponseEntity<>(workSpaceService.evaluationPeriodListByUser(), HttpStatus.OK);
    }
}

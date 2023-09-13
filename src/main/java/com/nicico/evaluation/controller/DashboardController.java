package com.nicico.evaluation.controller;

import com.nicico.copper.core.SecurityUtil;
import com.nicico.copper.sse.SSEEngine;
import com.nicico.evaluation.dto.EvaluationDTO;
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

    private final SSEEngine sseEngine;
    private final IWorkSpaceService workSpaceService;

    /**
     * @return that contain user FullName
     */
    @GetMapping(value = "/work-space/user-full-name")
    public ResponseEntity<String> workSpaceUserFullName() {
        return new ResponseEntity<>(SecurityUtil.getFullName(), HttpStatus.OK);
    }

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

//    @GetMapping("/work-space/alarm-notification")
//    public SseEmitter workSpaceAlarmNotification(@RequestBody List<String> workSpaceCodeList) {
//        List<WorkSpaceDTO.Info> alarmList = workSpaceService.workSpaceAlarm(workSpaceCodeList);
//        if (!alarmList.isEmpty()) {
//            SseEmitter emitter = sseEngine.create();
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.execute(() -> {
//                try {
//                    int i = 0;
//                    while (i < 3) {
//                        emitter.send(alarmList);
//                        log.info("========>" + alarmList);
//                        i++;
//                    }
//                    try {
//                        Thread.sleep(10800);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    emitter.complete();
//                } catch (IOException e) {
//                    emitter.completeWithError(e);
//                }
//            });
//            executor.shutdown();
//            return emitter;
//        }
//        return null;
//    }

    /**
     * @return that contain list of evaluationPeriodList By User
     */
    @GetMapping(value = "/evaluation-period/list")
    public ResponseEntity<EvaluationDTO.SpecResponse> evaluationPeriodListByUser(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(workSpaceService.evaluationPeriodListByUser(count, startIndex), HttpStatus.OK);
    }

    /**
     * @return that result of my evaluation by evaluationPeriodId
     */
    @GetMapping(value = "/my-evaluation/{evaluationPeriodId}")
    public ResponseEntity<EvaluationDTO.EvaluationAverageScoreData> evaluationAverageScoreDataByUser(@PathVariable Long evaluationPeriodId) {
        return new ResponseEntity<>(workSpaceService.evaluationAverageScoreDataByUser(evaluationPeriodId), HttpStatus.OK);
    }

}

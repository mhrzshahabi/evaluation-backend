package com.nicico.evaluation.controller;

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
     * @return MeritComponentDTO.SpecResponse that contain list of MeritComponentDTO and the number of total entity
     */
    @GetMapping(value = "/work-space/list")
    public ResponseEntity<List<WorkSpaceDTO.Info>> workSpaceList() {
        return new ResponseEntity<>(workSpaceService.workSpaceList(), HttpStatus.OK);
    }
}

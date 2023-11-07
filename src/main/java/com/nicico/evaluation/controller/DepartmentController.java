package com.nicico.evaluation.controller;

import com.nicico.evaluation.dto.DepartmentDTO;
import com.nicico.evaluation.iservice.IDepartmentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Api(value = "Department")
@RestController
@RequestMapping(value = "/api/department")
public class DepartmentController {

    private final IDepartmentService service;

    @GetMapping(value = "/mojtame/spec-list")
    public ResponseEntity<DepartmentDTO.SpecResponse> mojtameSpecList(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                      @RequestParam(value = "count", required = false, defaultValue = "30") Integer count) {
        return new ResponseEntity<>(service.mojtameList(startIndex, count), HttpStatus.OK);
    }

    @GetMapping(value = "/moavenat/spec-list")
    public ResponseEntity<DepartmentDTO.SpecResponse> moavenatSpecList(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                       @RequestParam(value = "count", required = false, defaultValue = "30") Integer count) {
        return new ResponseEntity<>(service.moavenatList(startIndex, count), HttpStatus.OK);
    }

    @GetMapping(value = "/omor/spec-list")
    public ResponseEntity<DepartmentDTO.SpecResponse> omorSpecList(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                   @RequestParam(value = "count", required = false, defaultValue = "30") Integer count) {
        return new ResponseEntity<>(service.omorList(startIndex, count), HttpStatus.OK);
    }

    @GetMapping(value = "/ghesmat/spec-list")
    public ResponseEntity<DepartmentDTO.SpecResponse> ghesmatSpecList(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                      @RequestParam(value = "count", required = false, defaultValue = "30") Integer count) {
        return new ResponseEntity<>(service.ghesmatList(startIndex, count), HttpStatus.OK);
    }
}

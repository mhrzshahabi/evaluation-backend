package com.nicico.evaluation.controller;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.service.GradeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@Api(value = "Grade")
@RestController
@RequestMapping(value = "/anonymous/api/grade")
public class GradeController {

    private final GradeService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<GradeDTO.Info> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<GradeDTO.Info>> list() {
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }


    @GetMapping(value = "/spec-list")
    public ResponseEntity<TotalResponse<GradeDTO.Info>> search(@RequestParam MultiValueMap<String, String> request) {
        final NICICOCriteria nicicoCriteria = NICICOCriteria.of(request);
        return new ResponseEntity<>(service.search(nicicoCriteria), HttpStatus.OK);
    }
}

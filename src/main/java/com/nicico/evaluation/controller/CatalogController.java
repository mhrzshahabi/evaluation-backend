package com.nicico.evaluation.controller;

import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.utility.EvaluationConstant;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/anonymous/api/catalog")
@Validated
@AllArgsConstructor
public class CatalogController {

    private final ICatalogService service;

    @GetMapping(value = "/level-def-list")
    public ResponseEntity<List<CatalogDTO.Info>> list() {
        return new ResponseEntity<>(service.list(EvaluationConstant.LEVEL_DEF), HttpStatus.OK);
    }

}

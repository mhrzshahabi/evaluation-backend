package com.nicico.evaluation.controller;

import com.nicico.evaluation.dto.OrganizationDTO;
import com.nicico.evaluation.iservice.IOrganizationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Api(value = "Organization")
@RestController
@RequestMapping(value = "/api/organization")
public class OrganizationController {
    private final IOrganizationService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<OrganizationDTO.Info>> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<OrganizationDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

}

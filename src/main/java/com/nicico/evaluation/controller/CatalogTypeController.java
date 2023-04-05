package com.nicico.evaluation.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.iservice.ICatalogTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/catalog-type")
public class CatalogTypeController {

    private final ICatalogTypeService catalogTypeService;

    @Loggable
    @PostMapping
    public ResponseEntity create(@RequestBody CatalogTypeDTO.Create create) {
        try {
            return new ResponseEntity<>(catalogTypeService.create(create), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Loggable
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        catalogTypeService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Loggable
    @GetMapping("/test")
    public ResponseEntity<CatalogTypeDTO.SpecResponse> getTestAPI() {
        CatalogTypeDTO.Info info = new CatalogTypeDTO.Info();
        List<CatalogTypeDTO.Info> data = new ArrayList<>();
        info.setId(1L).setCode("ABC").setTitle("A Title");
        data.add(info);
        CatalogTypeDTO.SpecResponse specResponse = new CatalogTypeDTO.SpecResponse();
        CatalogTypeDTO.Response response = new CatalogTypeDTO.Response();
        response.setData(data).setStartRow(0).setEndRow(1).setTotalRows(1);
        specResponse.setResponse(response);
        return new ResponseEntity<>(specResponse, HttpStatus.OK);
    }
}

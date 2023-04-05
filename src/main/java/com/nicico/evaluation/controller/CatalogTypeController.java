package com.nicico.evaluation.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.dto.grid.GridResponse;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.enums.ErrorType;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.exception.NotFoundException;
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
@RequestMapping(value = "/anonymous/api/catalog-type")
public class CatalogTypeController {

    private final ICatalogTypeService catalogTypeService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CatalogTypeDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(catalogTypeService.getById(id), HttpStatus.OK);
    }

    @Loggable
    @PostMapping
    public ResponseEntity create(@RequestBody CatalogTypeDTO.Create create) {
        try {
            return new ResponseEntity<>(catalogTypeService.create(create), HttpStatus.OK);
        } catch (NotFoundException ex) {
            throw new EvaluationHandleException(ErrorType.NotFound, "", ex.getMessage());
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
    public ResponseEntity<TotalResponse<CatalogTypeDTO.Info>> getTestAPI() {
        CatalogTypeDTO.Info info = new CatalogTypeDTO.Info();
        List<CatalogTypeDTO.Info> data = new ArrayList<>();
        info.setId(1L).setCode("ABC").setTitle("A Title");
        data.add(info);

        GridResponse<CatalogTypeDTO.Info> gridResponse = new GridResponse<>();
        gridResponse.setData(data);
        TotalResponse<CatalogTypeDTO.Info> totalResponse = new TotalResponse<>(gridResponse);
        return new ResponseEntity<>(totalResponse, HttpStatus.OK);
    }
}

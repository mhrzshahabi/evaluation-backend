package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.IBatchService;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Api(value = "Batch")
@RestController
@RequestMapping(value = "/api/batch")
public class BatchController {

    private final IBatchService service;

    /**
     * @param id is the batch id
     * @return BatchDTO.Info is the single batch entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<BatchDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return BatchDTO.SpecResponse that contain list of BatchDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<BatchDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create batch entity
     * @return BatchDTO.Info is the saved batch entity
     */
    @PostMapping
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody BatchDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<BatchDTO.Info> is the list of batch entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<BatchDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                        @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                        @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<BatchDTO.Info> data = service.search(request);
        final BatchDTO.Response response = new BatchDTO.Response();
        final BatchDTO.SpecResponse specRs = new BatchDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @param batchId    is the batch id
     * @return TotalResponse<BatchDetailDTO.Info> is the list of batchDetail entity that match the criteria
     */
    @GetMapping(value = "/detail/{batchId}")
    public ResponseEntity<BatchDetailDTO.SpecResponse> getDetailByBatchId(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                                @RequestBody List<FilterDTO> criteria, @PathVariable Long batchId) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<BatchDetailDTO.Info> data = service.batchDetailSearch(request, batchId);
        final BatchDetailDTO.Response response = new BatchDetailDTO.Response();
        final BatchDetailDTO.SpecResponse specRs = new BatchDetailDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

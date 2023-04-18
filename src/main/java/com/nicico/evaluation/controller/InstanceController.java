package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.iservice.IInstanceService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/instance")
@Api("Instance Api")
@Validated
@AllArgsConstructor
public class InstanceController {

    private final IInstanceService service;

    @GetMapping(value = "/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        String currentDateTime = new SimpleDateFormat("yyyy/MM/dd___HH-mm-ss").format(new Date());

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=data_" + currentDateTime + ".xlsx";

        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, Integer.MAX_VALUE, 0);
        ByteArrayOutputStream excelByteData = service.exportAsExcel(request);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(excelByteData.toByteArray());
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return InstanceDTO.SpecResponse that contain list of instanceInfoDto and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<InstanceDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param id is the instance id
     * @return InstanceDTO.Info is the single instance entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<InstanceDTO.Info> get(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create instance entity
     * @return GroupDTOInfo is the saved instance entity
     */
    @PostMapping
    public ResponseEntity<InstanceDTO.Info> create(@Valid @RequestBody InstanceDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update instance entity
     * @return GroupDTOInfo is the updated instance entity
     */
    @PutMapping
    public ResponseEntity<InstanceDTO.Info> update(@Valid @RequestBody InstanceDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the instance id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria is the key value pair for criteria
     * @return TotalResponse<InstanceDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<InstanceDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                           @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                           @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<InstanceDTO.Info> data = service.search(request);
        final InstanceDTO.Response response = new InstanceDTO.Response();
        final InstanceDTO.SpecResponse specRs = new InstanceDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

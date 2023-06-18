package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.PersonnelDTO;
import com.nicico.evaluation.iservice.IPersonnelService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Api(value = "Personnel")
@RestController
@RequestMapping(value = "/api/personnel")
public class PersonnelController {

    private final IPersonnelService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonnelDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/by-national-code/{nationalCode}")
    public ResponseEntity<PersonnelDTO.Info> getByNationalCode(@PathVariable String nationalCode) {
        return new ResponseEntity<>(service.getByNationalCode(nationalCode), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<PersonnelDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<PersonnelDTO.Info> is the list of PersonnelInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<PersonnelDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                            @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                            @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        var data = service.search(request);
        final PersonnelDTO.Response response = new PersonnelDTO.Response();
        final PersonnelDTO.SpecResponse specRs = new PersonnelDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

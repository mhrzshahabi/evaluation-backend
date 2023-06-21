package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.PersonDTO;
import com.nicico.evaluation.feignClient.HrmFeignClient;
import com.nicico.evaluation.iservice.IPersonService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Api(value = "Person")
@RestController
@RequestMapping(value = "/api/person")
public class PersonController {

    private final IPersonService service;
    private final HrmFeignClient hrmFeignClient;

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/image-profile/{nationalCode}")
    public ResponseEntity<String> getProfileImage(HttpServletRequest httpServletRequest, @PathVariable String nationalCode) {
        return new ResponseEntity<>(hrmFeignClient.getPersonProfileByNationalCode(nationalCode, httpServletRequest.getHeader("Authorization")).getImageProfile(), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<PersonDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<PersonDTO.Info> is the list of PersonInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<PersonDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                         @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                         @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        var data = service.search(request);
        final PersonDTO.Response response = new PersonDTO.Response();
        final PersonDTO.SpecResponse specRs = new PersonDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

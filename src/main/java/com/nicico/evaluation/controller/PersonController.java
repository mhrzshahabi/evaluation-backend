package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.PersonDTO;
import com.nicico.evaluation.iservice.IPersonService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Api(value = "Person")
@RestController
@RequestMapping(value = "/api/person")
public class PersonController {

    private final IPersonService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
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
        final List<SearchDTO.CriteriaRq> finalCriteriaRqList = new ArrayList<>();

        Optional<SearchDTO.CriteriaRq> criteriaFullName = request.getCriteria().getCriteria().stream()
                .filter(q -> q.getFieldName().equals("fullName")).findFirst();

        List<SearchDTO.CriteriaRq> otherCriteriaRqList = request.getCriteria().getCriteria().stream()
                .filter(q -> !q.getFieldName().equals("fullName")).toList();

        if (criteriaFullName.isPresent()) {

            String value = criteriaFullName.get().getValue().get(0).toString();

            final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
            final SearchDTO.CriteriaRq firstNameCriteriaRq = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.contains)
                    .setFieldName("firstName")
                    .setValue(value);

            final SearchDTO.CriteriaRq lastNameCriteriaRq = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.contains)
                    .setFieldName("lastName")
                    .setValue(value);

            criteriaRqList.add(firstNameCriteriaRq);
            criteriaRqList.add(lastNameCriteriaRq);

            final SearchDTO.CriteriaRq criteriaRqList1 = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.or)
                    .setCriteria(criteriaRqList);

            finalCriteriaRqList.add(criteriaRqList1);
        }

        if (!otherCriteriaRqList.isEmpty()) {
            finalCriteriaRqList.addAll(otherCriteriaRqList);
        }

        final SearchDTO.CriteriaRq finalCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(finalCriteriaRqList);
        request.setCriteria(finalCriteriaRq);

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

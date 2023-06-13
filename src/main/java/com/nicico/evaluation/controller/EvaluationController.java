package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationService;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/evaluation")
@Api("Evaluation Api")
@Validated
@AllArgsConstructor
public class EvaluationController {

    private final IEvaluationService service;
    private final ICatalogService catalogService;
    private final ResourceBundleMessageSource messageSource;

    /**
     * @param id is the evaluation id
     * @return EvaluationDTO.Info  is the single evaluation entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EvaluationDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return EvaluationDTO.SpecResponse that contain list of EvaluationInfo and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<EvaluationDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create evaluation entity
     * @return EvaluationDTOInfo is the saved evaluation entity
     */
    @PostMapping
    public ResponseEntity<EvaluationDTO.Info> create(@Valid @RequestBody EvaluationDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is the model of input for create evaluation entity
     * @return EvaluationDTOInfo is the saved evaluation entity
     */
    @PostMapping(value = "/create-list")
    public ResponseEntity<List<EvaluationDTO.Info>> createList(@Valid @RequestBody List<EvaluationDTO.CreateList> request) {
        return new ResponseEntity<>(service.createList(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update evaluation entity
     * @return EvaluationDTOInfo is the updated evaluation entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<EvaluationDTO.Info> update(@PathVariable Long id, @Valid @RequestBody EvaluationDTO.Update request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the evaluation id for delete
     * @return status code only
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@Validated @PathVariable Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException violationException) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null, messageSource.getMessage("exception.integrity.constraint", null, locale));
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @param ChangeStatusDTO is id of evaluation for change status and is next or previous for change status
     * @return Boolean is the result of function
     */
    @PostMapping(value = "/change-status")
    public ResponseEntity<BaseResponse> changeStatus(@Valid @RequestBody EvaluationDTO.ChangeStatusDTO ChangeStatusDTO) {
        BaseResponse response = service.changeStatus(ChangeStatusDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<EvaluationDTO.Info> is the list of EvaluationInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<EvaluationDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                             @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                             @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationDTO.Info> data = service.search(request);
        final EvaluationDTO.Response response = new EvaluationDTO.Response();
        final EvaluationDTO.SpecResponse specRs = new EvaluationDTO.SpecResponse();
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
     * @return TotalResponse<EvaluationDTO.Info> is the list of EvaluationInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list/assessorNationalCode")
    public ResponseEntity<EvaluationDTO.SpecResponse> searchByAssessorNationalCode(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                   @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                                   @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        boolean isAdmin = SecurityUtil.isAdmin();
        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();

        if (!isAdmin) {
            final SearchDTO.CriteriaRq nationalCodeCriteriaRq = new SearchDTO.CriteriaRq()
                    .setOperator(EOperator.equals)
                    .setFieldName("assessorNationalCode")
                    .setValue(SecurityUtil.getNationalCode());
            criteriaRqList.add(nationalCodeCriteriaRq);
        }
        final SearchDTO.CriteriaRq statusCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.notEqual)
                .setFieldName("statusCatalogId")
                .setValue(catalogService.getByCode("Initial-registration").getId());

        criteriaRqList.add(statusCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);


        SearchDTO.SearchRs<EvaluationDTO.Info> data = service.search(request);
        final EvaluationDTO.Response response = new EvaluationDTO.Response();
        final EvaluationDTO.SpecResponse specRs = new EvaluationDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

    
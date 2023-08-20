package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationItemService;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExceptionUtil;
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
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/api/evaluationItem")
@Api("EvaluationItem Api")
@Validated
@AllArgsConstructor
public class EvaluationItemController {

    private final ExceptionUtil exceptionUtil;
    private final IEvaluationItemService service;
    private final ResourceBundleMessageSource messageSource;

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return EvaluationItemDTO.SpecResponse that contain list of evaluationItemInfoDto and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<EvaluationItemDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param id is the evaluationItem id
     * @return EvaluationItemDTO.Info  is the single evaluationItem entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EvaluationItemDTO.Info> get(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.get(id), HttpStatus.OK);
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound);
        }
    }

    /**
     * @param request is the model of input for create evaluationItem entity
     * @return EvaluationItemDTOInfo is the saved evaluationItem entity
     */
    @PostMapping
    public ResponseEntity<List<EvaluationItemDTO.Info>> create(@Valid @RequestBody List<EvaluationItemDTO.Create> request) {
        return new ResponseEntity<>(service.createAll(request), HttpStatus.CREATED);
    }

    /**
     * @param assessPostCode is the postCode of asses
     * @return List<EvaluationItemDTO.CreateItemInfo> is the list of data that need for create evaluation Item
     */
    @GetMapping(value = "/getEvaluationItemInfo")
    public ResponseEntity<List<EvaluationItemDTO.CreateItemInfo>> get(@RequestParam String assessPostCode) {
        if (assessPostCode.contains("/"))
            assessPostCode = assessPostCode.substring(0, assessPostCode.indexOf("/"));
        return new ResponseEntity<>(service.getInfoByAssessPostCodeForCreate(assessPostCode), HttpStatus.OK);
    }

    /**
     * @param id is the id of evaluation
     * @return List<EvaluationItemDTO.CreateItemInfo> is the list of data that need for create evaluation Item
     */
    @GetMapping(value = "/getEvaluationItemUpdateInfo")
    public ResponseEntity<List<EvaluationItemDTO.CreateItemInfo>> getUpdateInfo(@RequestParam Long id) {
        return new ResponseEntity<>(service.getInfoByEvaluationIdForUpdate(id), HttpStatus.OK);
    }

    /**
     * @param request is  the model of input for update evaluationItem entity
     * @return EvaluationItemDTOInfo is the updated evaluationItem entity
     */
    @PutMapping
    public ResponseEntity<List<EvaluationItemDTO.Info>> update(@Valid @RequestBody List<EvaluationItemDTO.Update> request) {
        return new ResponseEntity<>(service.updateAll(request), HttpStatus.OK);
    }

    /**
     * @param id is the evaluationItem id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> delete(@Validated @PathVariable Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException violationException) {
            final Locale locale = LocaleContextHolder.getLocale();
            String msg = exceptionUtil.getRecordsByParentId(violationException, id);
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null, messageSource.getMessage("exception.integrity.constraint", null, locale) + msg);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<EvaluationItemDTO.Info> is the list of evaluationItemInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<EvaluationItemDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                 @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                 @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationItemDTO.Info> data = service.search(request);
        final EvaluationItemDTO.Response response = new EvaluationItemDTO.Response();
        final EvaluationItemDTO.SpecResponse specRs = new EvaluationItemDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

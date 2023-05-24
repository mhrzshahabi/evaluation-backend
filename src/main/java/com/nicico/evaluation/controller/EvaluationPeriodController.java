package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationPeriodPostService;
import com.nicico.evaluation.iservice.IEvaluationPeriodService;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@RequiredArgsConstructor
@Api(value = " Evaluation Period")
@RestController
@RequestMapping(value = "/api/evaluation-period")
public class EvaluationPeriodController {

    private final IEvaluationPeriodService service;
    private final ResourceBundleMessageSource messageSource;
    private final IEvaluationPeriodPostService evaluationPeriodPostService;

    /**
     * @param id is the EvaluationPeriod id
     * @return EvaluationPeriodDTO.Info is the single EvaluationPeriod entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return EvaluationPeriodDTO.SpecResponse that contain list of EvaluationPeriodInfo and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<EvaluationPeriodDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create EvaluationPeriod entity
     * @return EvaluationPeriodDTO.Info is the saved EvaluationPeriod entity
     */
    @PostMapping
    public ResponseEntity<EvaluationPeriodDTO.Info> create(@Valid @RequestBody EvaluationPeriodDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is the model of input for createPost EvaluationPeriod entity
     * @return EvaluationPeriodDTO.Info is the saved EvaluationPeriod entity
     */
    @PostMapping(value = "/create-evaluation-period-post")
    public ResponseEntity<List<EvaluationPeriodPostDTO.Info>> createEvaluationPeriodPost(@RequestBody EvaluationPeriodDTO.CreatePost request) {
        return new ResponseEntity<>(service.createEvaluationPeriodPost(request.getId(), request.getPostCode()), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update EvaluationPeriod entity
     * @return EvaluationPeriodDTO.Info is the updated EvaluationPeriod entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<EvaluationPeriodDTO.Info> update(@PathVariable Long id, @Valid @RequestBody EvaluationPeriodDTO.Update request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the EvaluationPeriod id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<BaseResponse> delete(@Validated @PathVariable Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            service.delete(id);
            BaseResponse response = new BaseResponse();
            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null, messageSource.getMessage("exception.integrity.constraint", null, locale));
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @param evaluationPeriodId is the id of evaluationPeriod entity
     * @param postCode is the postCode
     * @return BaseResponse is the status code and message
     */
    @DeleteMapping(value = "/delete-evaluation-period-post")
    public ResponseEntity<BaseResponse> deleteEvaluationPeriodPost(@RequestParam Long evaluationPeriodId, @RequestParam String postCode) {
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            evaluationPeriodPostService.deleteByEvaluationPeriodIdAndPostCode(evaluationPeriodId, postCode);
            BaseResponse response = new BaseResponse();
            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<EvaluationPeriodDTO.Info> is the list of EvaluationPeriodInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<EvaluationPeriodDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                   @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                   @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationPeriodDTO.Info> data = service.search(request);
        final EvaluationPeriodDTO.Response response = new EvaluationPeriodDTO.Response();
        final EvaluationPeriodDTO.SpecResponse specRs = new EvaluationPeriodDTO.SpecResponse();
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
     * @return TotalResponse<EvaluationPeriodDTO.Info> is the list of EvaluationPeriodInfo entity that match the criteria
     */
    @PostMapping(value = "/active/spec-list")
    public ResponseEntity<EvaluationPeriodDTO.SpecResponse> activeEvaluationPeriodList(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                       @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,@RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);

        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
//        final SearchDTO.CriteriaRq postCodeCriteriaRq = new SearchDTO.CriteriaRq()
//                .setOperator(EOperator.greaterOrEqual)
//                .setFieldName("startDate")
//                .setValue(DateUtil.todayDate());
//
//        criteriaRqList.add(postCodeCriteriaRq);
//        criteriaRqList.add(request.getCriteria());
//
//        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
//                .setOperator(EOperator.and)
//                .setCriteria(criteriaRqList);
//        request.setCriteria(criteriaRq);


        SearchDTO.SearchRs<EvaluationPeriodDTO.Info> data = service.search(request);
        final EvaluationPeriodDTO.Response response = new EvaluationPeriodDTO.Response();
        final EvaluationPeriodDTO.SpecResponse specRs = new EvaluationPeriodDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }


}

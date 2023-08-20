package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationPeriodPostService;
import com.nicico.evaluation.iservice.IEvaluationPeriodService;
import com.nicico.evaluation.service.ExecuterService;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Api(value = " Evaluation Period")
@RestController
@RequestMapping(value = "/api/evaluation-period")
public class EvaluationPeriodController {

    private final ExecuterService executerService;
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
    public ResponseEntity<EvaluationPeriodDTO.Info> create(@Valid @RequestBody EvaluationPeriodDTO.Create request) throws ParseException {
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
    public ResponseEntity<EvaluationPeriodDTO.Info> update(@PathVariable Long id, @Valid @RequestBody EvaluationPeriodDTO.Update request) throws ParseException {
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
     * @param postCode           is the postCode
     * @return BaseResponse is the status code and message
     */
    @DeleteMapping(value = "/delete-evaluation-period-post")
    public ResponseEntity<BaseResponse> deleteEvaluationPeriodPost(@RequestParam Long evaluationPeriodId, @RequestParam String postCode) {
        final Locale locale = LocaleContextHolder.getLocale();
        evaluationPeriodPostService.deleteByEvaluationPeriodIdAndPostCode(evaluationPeriodId, postCode);
        BaseResponse response = new BaseResponse();
        response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
        return new ResponseEntity<>(response, HttpStatus.OK);
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
                                                                                       @RequestParam(value = "count", required = false, defaultValue = "30") Integer count, @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        CriteriaUtil.addCriteria(request, EOperator.lessOrEqual, "startDate", EOperator.and, DateUtil.todayDate());
        CriteriaUtil.addCriteria(request, EOperator.greaterOrEqual, "endDate", EOperator.and, DateUtil.todayDate());
        CriteriaUtil.addCriteria(request, EOperator.equals, "statusCatalog.code", EOperator.and, "period-awaiting-review");
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
     * @param ChangeStatusDTO is id of evaluation for change status and is next or previous for change status
     * @return BaseResponse is the result of function
     */
    @PostMapping(value = "/change-status")
    public ResponseEntity<BaseResponse> changeStatus(@Valid @RequestBody EvaluationDTO.ChangeStatusDTO ChangeStatusDTO) {
        return new ResponseEntity<>(service.changeStatus(ChangeStatusDTO), HttpStatus.OK);
    }

    /**
     * @param evaluationPeriodId is id of evaluation period for change status and is next or previous for change status
     * @return Boolean is the result of function
     */
    @PostMapping(value = "/validate-posts-of-period/{evaluationPeriodId}")
    public Boolean validatePostsOfPeriod(@PathVariable Long evaluationPeriodId) {
        return service.validatePosts(evaluationPeriodId);
    }

    /**
     * @param evaluationPeriodId is the key value
     * @return byte[] is the excel of error list of posts that are invalid
     */
    @SneakyThrows
    @GetMapping(value = "/export-error-list-excel/{evaluationPeriodId}")
    public ResponseEntity<byte[]> exportExcel(@PathVariable Long evaluationPeriodId) {
        return service.downloadExcel(evaluationPeriodId);
    }
}

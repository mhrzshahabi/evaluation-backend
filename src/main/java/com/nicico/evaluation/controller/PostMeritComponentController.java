package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IPostMeritComponentService;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExceptionUtil;
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
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Api(value = "Post MeritComponent")
@RestController
@RequestMapping(value = "/api/post-merit-component")
public class PostMeritComponentController {

    private final IPostMeritComponentService service;
    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;
    /**
     * @param id is the postMeritComponent id
     * @return PostMeritComponentDTO.Info is the single postMeritComponent entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostMeritComponentDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return PostMeritComponentDTO.SpecResponse that contain list of PostMeritComponentDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<PostMeritComponentDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create postMeritComponent entity
     * @return PostMeritComponentDTO.Info is the saved postMeritComponent entity
     */
    @PostMapping
    public ResponseEntity<PostMeritComponentDTO.Info> create(@Valid @RequestBody PostMeritComponentDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update postMeritComponent entity
     * @return PostMeritComponentDTO.Info is the updated postMeritComponent entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<PostMeritComponentDTO.Info> update(@PathVariable Long id, @Valid @RequestBody PostMeritComponentDTO.Update request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the postMeritComponent id for delete
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
     * @return TotalResponse<PostMeritComponentDTO.Info> is the list of postMeritComponentInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<PostMeritComponentDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                     @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                     @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<PostMeritComponentDTO.Info> data = service.search(request);
        final PostMeritComponentDTO.Response response = new PostMeritComponentDTO.Response();
        final PostMeritComponentDTO.SpecResponse specRs = new PostMeritComponentDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

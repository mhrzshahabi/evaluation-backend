package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IPostMeritInstanceService;
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
import java.util.Set;

@RequiredArgsConstructor
@Api(value = "Post Merit Instance")
@RestController
@RequestMapping(value = "/api/post-merit-instance")
public class PostMeritInstanceController {

    private final IPostMeritInstanceService service;
    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;

    /**
     * @param id is the postMeritInstance id
     * @return PostMeritInstanceDTO.Info is the single postMeritInstance entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostMeritInstanceDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return PostMeritInstanceDTO.SpecResponse that contain list of PostMeritInstanceDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<PostMeritInstanceDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create postMeritInstance entity
     * @return PostMeritInstanceDTO.Info is the saved postMeritInstance entity
     */
    @PostMapping
    public ResponseEntity<Set<PostMeritInstanceDTO.Info>> create(@Valid @RequestBody PostMeritInstanceDTO.CreateAll request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param id is the postMeritInstance id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> delete(@Validated @PathVariable Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            service.delete(id);
            return new ResponseEntity<>(messageSource.getMessage("message.successful.operation", null, locale), HttpStatus.OK);
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
     * @return TotalResponse<PostMeritInstanceDTO.Info> is the list of postMeritInstanceInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<PostMeritInstanceDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                    @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                    @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<PostMeritInstanceDTO.Info> data = service.search(request);
        final PostMeritInstanceDTO.Response response = new PostMeritInstanceDTO.Response();
        final PostMeritInstanceDTO.SpecResponse specRs = new PostMeritInstanceDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

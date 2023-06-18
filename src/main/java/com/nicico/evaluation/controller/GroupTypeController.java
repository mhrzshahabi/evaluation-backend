package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGroupTypeService;
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
@Api(value = "Group Type")
@RestController
@RequestMapping(value = "/api/group-type")
public class GroupTypeController {

    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;
    private final IGroupTypeService service;

    /**
     * @param id is the groupType id
     * @return GroupTypeDTO.Info is the single groupType entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupTypeDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return GroupTypeDTO.SpecResponse that contain list of GroupTypeDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<GroupTypeDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param groupId is the id of entity to every record
     * @return GroupTypeDTO.GroupTypeMaxWeight is the data for remain weight of every groupType
     */
    @GetMapping(value = "/groupTypeMaxWeight")
    public ResponseEntity<GroupTypeDTO.GroupTypeMaxWeight> getWeightInfoByGroupId(@RequestParam Long groupId) {
        return new ResponseEntity<>(service.getWeightInfoByGroupId(groupId), HttpStatus.CREATED);
    }

    /**
     * @param request is the model of input for create groupType entity
     * @return GroupTypeDTO.Info is the saved groupType entity
     */
    @PostMapping
    public ResponseEntity<GroupTypeDTO.Info> create(@Valid @RequestBody GroupTypeDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update groupType entity
     * @return GroupTypeDTO.Info is the updated groupType entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<GroupTypeDTO.Info> update(@PathVariable Long id, @Valid @RequestBody GroupTypeDTO.Update request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the groupType id for delete
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
     * @return TotalResponse<GroupTypeDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<GroupTypeDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                            @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                            @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<GroupTypeDTO.Info> data = service.search(request);
        final GroupTypeDTO.Response response = new GroupTypeDTO.Response();
        final GroupTypeDTO.SpecResponse specRs = new GroupTypeDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

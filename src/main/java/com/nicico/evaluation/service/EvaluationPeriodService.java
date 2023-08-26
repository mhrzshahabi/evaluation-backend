package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.EvaluationPeriodMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.model.EvaluationPeriod;
import com.nicico.evaluation.repository.CatalogRepository;
import com.nicico.evaluation.repository.EvaluationPeriodRepository;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.EvaluationConstant;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.nicico.evaluation.utility.EvaluationConstant.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class EvaluationPeriodService implements IEvaluationPeriodService {

    private final PageableMapper pageableMapper;
    private final EvaluationPeriodMapper evaluationPeriodMapper;
    private final EvaluationPeriodRepository evaluationPeriodRepository;
    private final IEvaluationPeriodPostService evaluationPeriodPostService;
    private final ICatalogService catalogService;
    private final CatalogRepository catalogRepository;
    private final ResourceBundleMessageSource messageSource;
    private final IPostService postService;
    private final IGroupTypeService groupTypeService;
    private final IKPITypeService kpiTypeService;
    private final IGroupTypeMeritService groupTypeMeritService;
    private final IPostMeritComponentService postMeritComponentService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod getWithPostInfo(Long id) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> postInfoEvaluationPeriods = evaluationPeriodPostService.getAllByEvaluationPeriodId(id);
        EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod evaluationPeriodInfoPost = evaluationPeriodMapper.entityToDtoInfoWithPostInfoEvaluationPeriod(evaluationPeriod);
        evaluationPeriodInfoPost.setPostInfoEvaluationPeriod(postInfoEvaluationPeriods);
        return evaluationPeriodInfoPost;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.Info get(Long id) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return evaluationPeriodMapper.entityToDtoInfo(evaluationPeriod);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public List<EvaluationPeriodDTO.Info> getAllByDateAssessment() {
       // evaluationPeriodRepository.findAllByStartDateAssessment();
//        return evaluationPeriodMapper.entityToDtoInfo(evaluationPeriod);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<EvaluationPeriod> evaluationPeriods = evaluationPeriodRepository.findAll(pageable);
        List<EvaluationPeriodDTO.Info> evaluationInfos = evaluationPeriodMapper.entityToDtoInfoList(evaluationPeriods.getContent());

        EvaluationPeriodDTO.Response response = new EvaluationPeriodDTO.Response();
        EvaluationPeriodDTO.SpecResponse specResponse = new EvaluationPeriodDTO.SpecResponse();

        if (evaluationInfos != null) {
            response.setData(evaluationInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) evaluationPeriods.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public SearchDTO.SearchRs<EvaluationPeriodDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(evaluationPeriodRepository, evaluationPeriodMapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_PERIOD')")
    public List<EvaluationPeriodPostDTO.Info> createEvaluationPeriodPost(Long id, Set<String> postCode) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return evaluationPeriodPostService.createAll(evaluationPeriod, postCode);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION_PERIOD')")
    public void deleteEvaluationPeriodPost(Long id, String postCode) {
        evaluationPeriodPostService.deleteByEvaluationPeriodIdAndPostCode(id, postCode);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.Info create(EvaluationPeriodDTO.Create dto) throws ParseException {
        validationDates(dto);
        try {
            EvaluationPeriod evaluationPeriod = evaluationPeriodMapper.dtoCreateToEntity(dto);
            evaluationPeriod.setStatusCatalogId(catalogService.getByCode("period-initial-registration").getId());
            EvaluationPeriod save = evaluationPeriodRepository.save(evaluationPeriod);
            if (dto.getPostCode() != null && !dto.getPostCode().isEmpty())
                evaluationPeriodPostService.createAll(save, dto.getPostCode());
            return evaluationPeriodMapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION_PERIOD')")
    public EvaluationPeriodDTO.Info update(Long id, EvaluationPeriodDTO.Update dto) throws ParseException {
        validationDates(dto);
        try {
            EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() ->
                    new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
            if (Objects.nonNull(evaluationPeriod) && !evaluationPeriod.getStatusCatalog().getCode().equals(PERIOD_INITIAL_REGISTRATION))
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable, "",
                        messageSource.getMessage("exception.update.evaluation.period.Initial-registration", null, LocaleContextHolder.getLocale()));

            evaluationPeriodMapper.update(evaluationPeriod, dto);
            EvaluationPeriod save = evaluationPeriodRepository.save(evaluationPeriod);
            return evaluationPeriodMapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION_PERIOD')")
    public void delete(Long id) {
        EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        evaluationPeriodPostService.deleteByEvaluationPeriodId(evaluationPeriod.getId());
        evaluationPeriodRepository.delete(evaluationPeriod);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION_PERIOD')")
    public BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO) {
        BaseResponse response = new BaseResponse();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            List<Long> ids = changeStatusDTO.getEvaluationIds();
            for (Long id : ids) {
                Optional<EvaluationPeriod> optionalEvaluationPeriod = evaluationPeriodRepository.findById(id);
                if (optionalEvaluationPeriod.isPresent()) {
                    EvaluationPeriod evaluationPeriod = optionalEvaluationPeriod.get();
                    switch (changeStatusDTO.getStatus().toLowerCase(Locale.ROOT)) {
                        case "next" -> {
                            if (evaluationPeriod.getStatusCatalog().getCode() != null && evaluationPeriod.getStatusCatalog().getCode().equals(PERIOD_INITIAL_REGISTRATION)) {
                                Optional<Catalog> optionalCatalog = catalogRepository.findByCode(PERIOD_AWAITING_REVIEW);
                                optionalCatalog.ifPresent(catalog -> evaluationPeriod.setStatusCatalogId(catalog.getId()));

                            } else if (evaluationPeriod.getStatusCatalog().getCode() != null && evaluationPeriod.getStatusCatalog().getCode().equals(PERIOD_AWAITING_REVIEW)) {
                                Optional<Catalog> optionalCatalog = catalogRepository.findByCode(PERIOD_FINALIZED);
                                optionalCatalog.ifPresent(catalog -> evaluationPeriod.setStatusCatalogId(catalog.getId()));
                            }
                            evaluationPeriodRepository.save(evaluationPeriod);
                            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
                            response.setStatus(200);
                        }
                        case "previous" -> {
                            if (evaluationPeriod.getStatusCatalog().getCode() != null && evaluationPeriod.getStatusCatalog().getCode().equals(PERIOD_FINALIZED)) {
                                Optional<Catalog> optionalCatalog = catalogRepository.findByCode(PERIOD_AWAITING_REVIEW);
                                optionalCatalog.ifPresent(catalog -> evaluationPeriod.setStatusCatalogId(catalog.getId()));
                            } else if (evaluationPeriod.getStatusCatalog().getCode() != null && evaluationPeriod.getStatusCatalog().getCode().equals(PERIOD_AWAITING_REVIEW)) {
                                Optional<Catalog> optionalCatalog = catalogRepository.findByCode(PERIOD_INITIAL_REGISTRATION);
                                optionalCatalog.ifPresent(catalog -> evaluationPeriod.setStatusCatalogId(catalog.getId()));

                            }
                            evaluationPeriodRepository.save(evaluationPeriod);
                            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
                            response.setStatus(200);
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + changeStatusDTO.getStatus().toLowerCase(Locale.ROOT));
                    }
                }
            }
            return response;
        } catch (Exception e) {
            response.setMessage(messageSource.getMessage("exception.un-managed", null, locale));
            response.setStatus(EvaluationHandleException.ErrorType.EvaluationDeadline.getHttpStatusCode());
            return response;
        }
    }

    @Override
    @Transactional
    public Boolean validatePosts(Long evaluationPeriodId) {

        List<String> postCodes = evaluationPeriodPostService.getAllByEvaluationPeriodId(evaluationPeriodId).
                stream().map(EvaluationPeriodPostDTO.PostInfoEvaluationPeriod::getPostCode).toList();
        List<PostDTO.Info> allPostsHasNotGrade = postService.getPostGradeHasNotGroupByPeriodId(evaluationPeriodId);
        if (!allPostsHasNotGrade.isEmpty())
            return false;

        List<PostDTO.Info> groupHasNotGroupTypeByPostCode = postService.getGroupHasNotGroupTypeByPeriodId(evaluationPeriodId);
        if (!groupHasNotGroupTypeByPostCode.isEmpty())
            return false;

        List<GroupTypeDTO.Info> allGroupTypeByGroupPostCode = groupTypeService.getAllByPeriodId(evaluationPeriodId);
        Map<Long, List<GroupTypeDTO.Info>> allGroupTypeMap = allGroupTypeByGroupPostCode.stream().collect(Collectors.groupingBy(GroupTypeDTO::getGroupId));
        int typeSize = kpiTypeService.findAll().size();
        AtomicReference<Boolean> result = new AtomicReference<>(Boolean.TRUE);
        allGroupTypeMap.forEach((groupId, groupTypes) -> {
            if (groupTypes.size() != typeSize) {
                result.set(false);
                return;
            }
            double totalWeight = groupTypes.stream().mapToDouble(GroupTypeDTO::getWeight).sum();
            if (totalWeight != 100) {
                result.set(false);
                return;
            }
            Long statusCatalogId = catalogService.getByCode(REVOKED_MERIT).getId();
            groupTypes.forEach(groupType -> {
                if (!groupType.getKpiType().getTitle().equals(EvaluationConstant.KPI_TYPE_TITLE_OPERATIONAL)) {
                    List<GroupTypeMeritDTO.Info> allByGroupType = groupTypeMeritService.getAllByGroupTypeIdAndMeritStatusId(groupType.getId(), statusCatalogId);
                    if (allByGroupType.isEmpty()) {
                        result.set(false);
                        return;
                    }
                    double totalWeightGroupTypeMerit = allByGroupType.stream().mapToDouble(GroupTypeMeritDTO::getWeight).sum();
                    if (totalWeightGroupTypeMerit != 100) {
                        result.set(false);
                    }
                } else {
                    postCodes.forEach(postCode -> {
                        List<EvaluationItemDTO.MeritTupleDTO> byPostCode = postMeritComponentService.getByPostCodeAndMeritStatus(postCode, statusCatalogId);
                        if (byPostCode.isEmpty()) {
                            result.set(false);
                            return;
                        }
                        double totalWeightPostMerit = byPostCode.stream().mapToDouble(EvaluationItemDTO.MeritTupleDTO::getWeight).sum();
                        if (totalWeightPostMerit != 100) {
                            result.set(false);
                            return;
                        }
                    });
                }
            });
        });
        return result.get();
    }

    @SneakyThrows
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public ResponseEntity<byte[]> downloadExcel(Long evaluationPeriodId) throws NoSuchFieldException, IllegalAccessException {

        List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostList = createInvalidPostList(evaluationPeriodId);
        byte[] body = BaseService.exportExcelByList(invalidPostList, null, "گزارش لیست پست ها");
        ExcelGenerator.ExcelDownload excelDownload = new ExcelGenerator.ExcelDownload(body);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                .body(excelDownload.getContent());
    }

    private List<EvaluationPeriodPostDTO.InvalidPostExcel> createInvalidPostList(Long evaluationPeriodId) {

        List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList = new ArrayList<>();
        validateGradeHasGroup(evaluationPeriodId, invalidPostExcelList);
        validateGroupGradeHasNotGroupType(evaluationPeriodId, invalidPostExcelList);

        List<GroupTypeDTO.Info> allGroupTypeByPostCode = groupTypeService.getAllByPeriodId(evaluationPeriodId);
        Map<Long, List<GroupTypeDTO.Info>> allGroupTypeMap = allGroupTypeByPostCode.stream().collect(Collectors.groupingBy(GroupTypeDTO::getGroupId));
        validateAllGroupTypeMeritPost(evaluationPeriodId, invalidPostExcelList, allGroupTypeMap);
        return invalidPostExcelList;
    }

    private void validateAllGroupTypeMeritPost(Long evaluationPeriodId, List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList, Map<Long, List<GroupTypeDTO.Info>> allGroupTypeMap) {
        int typeSize = kpiTypeService.findAll().size();
        List<String> postCodes = evaluationPeriodPostService.getAllByEvaluationPeriodId(evaluationPeriodId).
                stream().map(EvaluationPeriodPostDTO.PostInfoEvaluationPeriod::getPostCode).toList();

        Long statusCatalogId = catalogService.getByCode(REVOKED_MERIT).getId();
        allGroupTypeMap.forEach((groupId, groupTypes) -> {
            /*check all kpiType define for these group*/
            validateAllGroupType(invalidPostExcelList, typeSize, groupTypes, groupTypes.size(), "exception.not.define.all.group-type.for.these.group");
            double totalWeight = groupTypes.stream().mapToDouble(GroupTypeDTO::getWeight).sum();
            /*check totalWeight is 100*/
            validateAllGroupType(invalidPostExcelList, 100, groupTypes, totalWeight, "exception.invalid.group.type.weight.for.these.group");

            groupTypes.forEach(groupType -> {
                if (!groupType.getKpiType().getTitle().equals(EvaluationConstant.KPI_TYPE_TITLE_OPERATIONAL)) {
                    List<GroupTypeMeritDTO.Info> allGroupTypeMeritByGroupType = groupTypeMeritService.getAllByGroupTypeIdAndMeritStatusId(groupType.getId(), statusCatalogId);
                    /*check define meritComponents for all groupTypes that are Behavioral or Development*/
                    validateGroupTypeMeritIsDefine(invalidPostExcelList, groupTypes, allGroupTypeMeritByGroupType);
                    /*check total groupTypeMerit's weight for all groupTypes that are Behavioral or Development*/
                    validateGroupTypeMeritWeight(invalidPostExcelList, groupTypes, groupType, allGroupTypeMeritByGroupType);
                } else {
                    postCodes.forEach(postCode -> {
                        List<EvaluationItemDTO.MeritTupleDTO> byPostCode = postMeritComponentService.getByPostCodeAndMeritStatus(postCode, statusCatalogId);
                        if (byPostCode.isEmpty())
                            validatePostMeritIsDefine(invalidPostExcelList, postCode);

                        double totalWeightPostMerit = byPostCode.stream().mapToDouble(EvaluationItemDTO.MeritTupleDTO::getWeight).sum();
                        if (totalWeightPostMerit != 100) {
                            EvaluationPeriodPostDTO.InvalidPostExcel notDefinePostMerit = new EvaluationPeriodPostDTO.InvalidPostExcel();
                            String errorMessage = messageSource.getMessage("exception.invalid.post-merit.total.weight",
                                    new Object[]{postCode}, LocaleContextHolder.getLocale());
                            notDefinePostMerit.setDescription(errorMessage);
                            notDefinePostMerit.setGroupPostCode(String.valueOf(postCode));
                            invalidPostExcelList.add(notDefinePostMerit);
                        }
                    });
                }
            });
//            List<String> groupPostCodesHasNotMerit = evaluationPeriodPostService.getAllByPeriodIdNotIn(evaluationPeriodId).stream().map(EvaluationPeriodPostDTO::getPostCode).toList();
            /*check define meritComponents for all posts that are Operational */
//            validatePostMeritIsDefine(invalidPostExcelList, groupPostCodesHasNotMerit);
            /*check postMeritComponent's weight for all posts that are Operational */
//            List<PostMeritComponentDTO.Info> allByPeriodIdIn = postMeritComponentService.getAllByPeriodIdIn(evaluationPeriodId);
//            validatePostMeritWeight(invalidPostExcelList, groupPostCodesHasNotMerit, allByPeriodIdIn);

        });
    }

    private void validatePostMeritWeight(List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList, List<String> postCodes, List<PostMeritComponentDTO.Info> postMerits) {
        Map<String, List<PostMeritComponentDTO.Info>> groupPostCodeList = postMerits.stream().collect(Collectors.groupingBy(PostMeritComponentDTO::getGroupPostCode));
        groupPostCodeList.forEach((groupPostCode, postMerit) -> {
            long totalWeightPostMerit = postMerit.stream().mapToLong(PostMeritComponentDTO::getWeight).sum();
            if (totalWeightPostMerit != 100) {
                EvaluationPeriodPostDTO.InvalidPostExcel inValidTotalPostMeritWeight = new EvaluationPeriodPostDTO.InvalidPostExcel();
                String errorMessage = messageSource.getMessage("exception.invalid.post-merit.total.weight",
                        new Object[]{postCodes}, LocaleContextHolder.getLocale());
                inValidTotalPostMeritWeight.setDescription(errorMessage);
                inValidTotalPostMeritWeight.setGroupPostCode(String.valueOf(postCodes));
                inValidTotalPostMeritWeight.setTotalWeightOperational(totalWeightPostMerit);
                invalidPostExcelList.add(inValidTotalPostMeritWeight);
            }
        });
    }

    private void validatePostMeritIsDefine(List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList, String groupPostCode) {

        EvaluationPeriodPostDTO.InvalidPostExcel notDefinePostMerit = new EvaluationPeriodPostDTO.InvalidPostExcel();
        String errorMessage = messageSource.getMessage("exception.not.define.post-merit.for.these.post",
                new Object[]{groupPostCode}, LocaleContextHolder.getLocale());
        notDefinePostMerit.setDescription(errorMessage);
        notDefinePostMerit.setGroupPostCode(String.valueOf(groupPostCode));
        invalidPostExcelList.add(notDefinePostMerit);

    }

    private void validateGroupTypeMeritWeight(List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList, List<GroupTypeDTO.Info> groupTypes, GroupTypeDTO.Info groupType, List<GroupTypeMeritDTO.Info> allGroupTypeMeritByGroupType) {
        long totalWeightGroupTypeMerit = allGroupTypeMeritByGroupType.stream().mapToLong(GroupTypeMeritDTO::getWeight).sum();
        if (totalWeightGroupTypeMerit != 100) {
            EvaluationPeriodPostDTO.InvalidPostExcel inValidTotalGroupTypeMeritWeight = new EvaluationPeriodPostDTO.InvalidPostExcel();
            String errorMessage = messageSource.getMessage("exception.invalid.group-type-merit.total.weight",
                    new Object[]{allGroupTypeMeritByGroupType.get(0).getGroupType().getGroup().getTitle(),
                            allGroupTypeMeritByGroupType.get(0).getGroupType().getKpiType().getTitle(),
                            allGroupTypeMeritByGroupType.get(0).getMeritComponent().getTitle()}, LocaleContextHolder.getLocale());
            inValidTotalGroupTypeMeritWeight.setDescription(errorMessage);
            inValidTotalGroupTypeMeritWeight.setGroupTypeTitle(groupTypes.get(0).getGroup().getTitle());
            inValidTotalGroupTypeMeritWeight.setPostGradeTitle(String.valueOf(groupTypes.get(0).getGroup().getGrade().stream().map(GradeDTO::getTitle).toList()));
            if (groupType.getKpiType().getTitle().equals(EvaluationConstant.KPI_TYPE_TITLE_DEVELOPMENT))
                inValidTotalGroupTypeMeritWeight.setTotalWeightDevelopment(totalWeightGroupTypeMerit);
            else if (groupType.getKpiType().getTitle().equals(EvaluationConstant.KPI_TYPE_TITLE_BEHAVIORAL))
                inValidTotalGroupTypeMeritWeight.setTotalWeightBehavioral(totalWeightGroupTypeMerit);

            invalidPostExcelList.add(inValidTotalGroupTypeMeritWeight);
        }
    }

    private void validateGroupTypeMeritIsDefine(List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList, List<GroupTypeDTO.Info> groupTypes, List<GroupTypeMeritDTO.Info> allGroupTypeMeritByGroupType) {
        if (allGroupTypeMeritByGroupType.isEmpty()) {
            EvaluationPeriodPostDTO.InvalidPostExcel notDefineMerit = new EvaluationPeriodPostDTO.InvalidPostExcel();
            String errorMessage = messageSource.getMessage("exception.not.define.group-type-merit.for.these.group-type",
                    new Object[]{groupTypes.get(0).getGroup().getTitle(), groupTypes.get(0).getKpiType().getTitle()}, LocaleContextHolder.getLocale());
            notDefineMerit.setDescription(errorMessage);
            notDefineMerit.setGroupTypeTitle(groupTypes.get(0).getGroup().getTitle());
            notDefineMerit.setPostGradeTitle(String.valueOf(groupTypes.get(0).getGroup().getGrade().stream().map(GradeDTO::getTitle).toList()));
            invalidPostExcelList.add(notDefineMerit);
        }
    }

    private void validateAllGroupType(List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList, int typeSize, List<GroupTypeDTO.Info> groupTypes, double size, String s) {
        if (size != typeSize) {
            EvaluationPeriodPostDTO.InvalidPostExcel notDefineAllType = new EvaluationPeriodPostDTO.InvalidPostExcel();
            String errorMessage = messageSource.getMessage(s,
                    new Object[]{groupTypes.get(0).getGroup().getTitle()}, LocaleContextHolder.getLocale());
            notDefineAllType.setDescription(errorMessage);
            notDefineAllType.setGroupTypeTitle(groupTypes.get(0).getGroup().getTitle());
            notDefineAllType.setPostGradeTitle(String.valueOf(groupTypes.get(0).getGroup().getGrade().stream().map(GradeDTO::getTitle).toList()));
            invalidPostExcelList.add(notDefineAllType);
        }
    }

    private void validateGroupGradeHasNotGroupType(Long evaluationPeriodId, List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList) {
        List<PostDTO.Info> groupHasNotGroupTypeByPostCode = postService.getGroupHasNotGroupTypeByPeriodId(evaluationPeriodId);
        if (!groupHasNotGroupTypeByPostCode.isEmpty()) {
            groupHasNotGroupTypeByPostCode.forEach(groupPost -> {
                EvaluationPeriodPostDTO.InvalidPostExcel groupGradeHaseNotGroupType = new EvaluationPeriodPostDTO.InvalidPostExcel();
                String errorMessage = messageSource.getMessage("exception.not.exist.group-type.for.these.group-grade", new Object[]{groupPost.getPostGradeTitle()}, LocaleContextHolder.getLocale());
                groupGradeHaseNotGroupType.setDescription(errorMessage);
                groupGradeHaseNotGroupType.setGroupPostCode(groupPost.getPostCode());
                groupGradeHaseNotGroupType.setPostTitle(groupPost.getPostTitle());
                groupGradeHaseNotGroupType.setPostGradeTitle(groupPost.getPostGradeTitle());
                invalidPostExcelList.add(groupGradeHaseNotGroupType);
            });
        }
    }

    private void validateGradeHasGroup(Long evaluationPeriodId, List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList) {
        List<PostDTO.Info> allPostGradeHasNotGroup = postService.getPostGradeHasNotGroupByPeriodId(evaluationPeriodId);
        if (!allPostGradeHasNotGroup.isEmpty()) {
            allPostGradeHasNotGroup.forEach(groupPost -> {
                EvaluationPeriodPostDTO.InvalidPostExcel gradeHaseNotGroup = new EvaluationPeriodPostDTO.InvalidPostExcel();
                String errorMessage = messageSource.getMessage("exception.not.exist.group-grade.for.these.post-codes",
                        new Object[]{groupPost.getPostCode(), groupPost.getPostGradeTitle()}, LocaleContextHolder.getLocale());
                gradeHaseNotGroup.setDescription(errorMessage);
                gradeHaseNotGroup.setGroupPostCode(groupPost.getPostCode());
                gradeHaseNotGroup.setPostTitle(groupPost.getPostTitle());
                gradeHaseNotGroup.setPostGradeTitle(groupPost.getPostGradeTitle());
                invalidPostExcelList.add(gradeHaseNotGroup);
            });
        }
    }

    private void validationDates(EvaluationPeriodDTO dto) throws ParseException {

        log.info("getStartDateAssessment " + changeToSpecialTime(dto.getStartDateAssessment()) + " / ");
        log.info("getStartDate " + changeToSpecialTime(dto.getStartDate()) + " / ");
        log.info("getEndDateAssessment " + changeToSpecialTime(dto.getEndDateAssessment()) + " / ");
        log.info("getEndDate " + changeToSpecialTime(dto.getEndDate()) + " / ");

        if (changeToSpecialTime(dto.getStartDateAssessment()).before(changeToSpecialTime(dto.getStartDate())) ||
                changeToSpecialTime(dto.getStartDateAssessment()).after(changeToSpecialTime(dto.getEndDate())) ||
                changeToSpecialTime(dto.getStartDateAssessment()).after(changeToSpecialTime(dto.getEndDateAssessment())) ||
                changeToSpecialTime(dto.getEndDateAssessment()).before(changeToSpecialTime(dto.getStartDate())) ||
                changeToSpecialTime(dto.getEndDateAssessment()).after(changeToSpecialTime(dto.getEndDate()))
        ) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotInEvaluationPeriodDuration, null,
                    messageSource.getMessage("message.not.in.evaluation.period.duration", null, LocaleContextHolder.getLocale()));
        }

        if (Objects.nonNull(dto.getValidationStartDate()) && Objects.nonNull(dto.getValidationEndDate())) {
            Calendar validationStartDateChanged = Calendar.getInstance();
            validationStartDateChanged.setTime(dto.getValidationStartDate());
            validationStartDateChanged.add(Calendar.DAY_OF_MONTH, 15);

            if (dto.getValidationStartDate().after(dto.getValidationEndDate()) ||
                    !Objects.equals(changeToSpecialTime(validationStartDateChanged.getTime()), changeToSpecialTime(dto.getStartDateAssessment()))) {
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotInEvaluationPeriodDuration, null,
                        messageSource.getMessage("exception.validation-date.not.is.invalid", null, LocaleContextHolder.getLocale()));
            }
        }
    }

    private Date changeToSpecialTime(Date date) throws ParseException {
//        log.info("date " + date + " / ");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        Date dated = new SimpleDateFormat("yyyy-MM-dd").parse(dateFormat.format(date));

        log.info("dated " + dated + " / ");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dated);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        log.info("calendar.getTime " + calendar.getTime() + " / ");
        return calendar.getTime();
    }
}

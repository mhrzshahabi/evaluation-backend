package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.copper.core.SecurityUtil;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.EvaluationMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.model.Evaluation;
import com.nicico.evaluation.model.Post;
import com.nicico.evaluation.repository.CatalogRepository;
import com.nicico.evaluation.repository.EvaluationRepository;
import com.nicico.evaluation.utility.EvaluationConstant;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.nicico.evaluation.utility.EvaluationConstant.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class EvaluationService implements IEvaluationService {

    @Value("${nicico.oauthBackend}")
    private String oAuthUrl;

    @Autowired
    @Qualifier("oauthToken")
    private RestTemplate restTemplateOAuth;

    private final EvaluationMapper mapper;
    private final IPostService postService;
    private final IBatchService batchService;
    private final PageableMapper pageableMapper;
    private final EvaluationRepository repository;
    private final CatalogRepository catalogRepository;
    private IEvaluationItemService evaluationItemService;
    private final ISpecialCaseService specialCaseService;
    private final ResourceBundleMessageSource messageSource;
    private IEvaluationPeriodService evaluationPeriodService;
    private final IMeritComponentService meritComponentService;
    private final IOrganizationTreeService organizationTreeService;
    private final ICatalogService catalogService;
    private final IGroupTypeService groupTypeService;
    private final IKPITypeService kpiTypeService;
    private final IGroupTypeMeritService groupTypeMeritService;
    private final IPostMeritComponentService postMeritComponentService;
    private final IEvaluationViewService evaluationViewService;

    @Autowired
    public void setEvaluationItemService(@Lazy IEvaluationItemService evaluationItemService) {
        this.evaluationItemService = evaluationItemService;
    }

    @Autowired
    public void setEvaluationPeriodService(@Lazy IEvaluationPeriodService evaluationPeriodService) {
        this.evaluationPeriodService = evaluationPeriodService;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Evaluation> evaluations = repository.findAll(pageable);
        List<EvaluationDTO.Info> evaluationInfos = mapper.entityToDtoInfoList(evaluations.getContent());

        EvaluationDTO.Response response = new EvaluationDTO.Response();
        EvaluationDTO.SpecResponse specResponse = new EvaluationDTO.SpecResponse();

        if (evaluationInfos != null) {
            response.setData(evaluationInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) evaluations.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.Info get(Long id) {
        Evaluation evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        Post assessPost = postService.getByPostCode(evaluation.getAssessPostCode());
        return mapper.entityToDtoInfo(evaluation, assessPost);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.Info getAllByPeriodIdAndAssessPostCode(Long periodId, String assessPostCode) {
        Evaluation evaluation = repository.findByEvaluationPeriodIdAndAssessPostCode(periodId, assessPostCode);
        if (Objects.nonNull(evaluation)) {
            Post assessPost = postService.getByPostCode(evaluation.getAssessPostCode());
            return mapper.entityToDtoInfo(evaluation, assessPost);
        } else
            return null;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public Evaluation getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION')")
    public EvaluationDTO.Info create(EvaluationDTO.Create dto) {
        Evaluation evaluation = mapper.dtoCreateToEntity(dto);
        evaluation = repository.save(evaluation);
        Post assessPost = postService.getByPostCode(evaluation.getAssessPostCode());
        return mapper.entityToDtoInfo(evaluation, assessPost);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION')")
    public List<EvaluationDTO.Info> createList(List<EvaluationDTO.CreateList> dto) {

        List<EvaluationDTO.Info> evaluationInfo = new ArrayList<>();
        List<Catalog> methodTypes = catalogRepository.findAllByCodeIn(List.of("Special-case", "Organizational-chart"));
        Catalog catalogStatus = catalogRepository.findByCode("Initial-registration").orElse(null);

        List<Object> specialCaseRevoked = new ArrayList<>();
        for (EvaluationDTO.CreateList evaluationCreate : dto) {
            List<SpecialCaseDTO.Info> specialCaseInfos = new ArrayList<>();
            Evaluation evaluation = new Evaluation();
            evaluation.setStatusCatalogId(Objects.requireNonNull(catalogStatus).getId());
            evaluation.setEvaluationPeriodId(evaluationCreate.getEvaluationPeriodId());
            OrganizationTreeDTO.InfoTree orgTreeInfo = organizationTreeService.getByPostCode(evaluationCreate.getPostCode());
            Evaluation evaluationExist =
                    repository.findByEvaluationPeriodIdAndAssessPostCode(evaluationCreate.getEvaluationPeriodId(), evaluationCreate.getPostCode());
            if (Objects.nonNull(evaluationExist))
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave, null,
                        messageSource.getMessage("exception.duplicated.evaluation", new Object[]{evaluationCreate.getPostCode()},
                                LocaleContextHolder.getLocale()));

            if (Objects.isNull(orgTreeInfo.getNationalCode()))
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, null,
                        messageSource.getMessage("exception.evaluation.assess-national-code.is.null", null, LocaleContextHolder.getLocale()));

            validateSpecialCase(methodTypes, specialCaseInfos, specialCaseRevoked, evaluationCreate, evaluation, orgTreeInfo);
            try {
                evaluation = repository.save(evaluation);
                Post assessPost = postService.getByPostCode(evaluation.getAssessPostCode());
                evaluationInfo.add(mapper.entityToDtoInfo(evaluation, assessPost));
            } catch (Exception exception) {
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave, null,
                        messageSource.getMessage("exception.bad.data", null, LocaleContextHolder.getLocale()));
            }
        }
        revokedSpecialCase(specialCaseRevoked);
        return evaluationInfo;
    }

    private void validateSpecialCase(List<Catalog> methodTypes, List<SpecialCaseDTO.Info> specialCaseInfos, List<Object>
            specialCaseRevoked, EvaluationDTO.CreateList evaluationCreate, Evaluation evaluation, OrganizationTreeDTO.InfoTree orgTreeInfo) {

        List<SpecialCaseDTO.Info> byAssessNationalCode = specialCaseService.
                getByAssessNationalCodeAndStatusCode(orgTreeInfo.getNationalCode(), SPECIAL_ACTIVE);

        SpecialCaseDTO.Info scInfo = getSpecialCaseInfo(evaluationCreate, byAssessNationalCode, specialCaseInfos, specialCaseRevoked);
        setSpecialCaseInfo(methodTypes, evaluationCreate, evaluation, orgTreeInfo, scInfo);
    }

    private void setSpecialCaseInfo(List<Catalog> methodTypes, EvaluationDTO.CreateList evaluationCreate, Evaluation evaluation, OrganizationTreeDTO.InfoTree orgTreeInfo, SpecialCaseDTO.Info scInfo) {
        if (Objects.nonNull(scInfo)) {
            evaluation.setAssessorPostCode(Objects.nonNull(scInfo.getAssessorPostCode()) ? scInfo.getAssessorPostCode() : orgTreeInfo.getPostParentCode());
            evaluation.setAssessorNationalCode(Objects.nonNull(scInfo.getAssessorNationalCode()) ? scInfo.getAssessorNationalCode() : orgTreeInfo.getNationalCodeParent());
            evaluation.setAssessorFullName(Objects.nonNull(scInfo.getAssessorFullName()) ? scInfo.getAssessorFullName() : orgTreeInfo.getFirstNameParent() + " " + orgTreeInfo.getLastNameParent());
            evaluation.setAssessFullName(scInfo.getAssessFullName());
            evaluation.setAssessNationalCode(scInfo.getAssessNationalCode());
            evaluation.setAssessPostCode(Objects.nonNull(scInfo.getAssessRealPostCode()) ? scInfo.getAssessRealPostCode() : evaluationCreate.getPostCode());
            evaluation.setSpecialCaseId(scInfo.getId());
            Long methodCatalogId = methodTypes.stream().filter(x -> x.getCode().equals("Special-case")).findFirst().orElseThrow().getId();
            evaluation.setMethodCatalogId(methodCatalogId);
        } else {

            if (Objects.isNull(orgTreeInfo.getNationalCodeParent()) && Objects.nonNull(orgTreeInfo.getPostPath())) {
                List<Long> postIds = Arrays.stream(orgTreeInfo.getPostPath().split("/")).map(Long::valueOf).toList();
                OrganizationTreeDTO.InfoTree orgTreeInfo1 = organizationTreeService.getByPostIds(postIds).stream().findFirst().orElseThrow();
                if (Objects.nonNull(orgTreeInfo1)) {
                    evaluation.setAssessorPostCode(orgTreeInfo1.getPostCode());
                    evaluation.setAssessorNationalCode(orgTreeInfo1.getNationalCode());
                    evaluation.setAssessorFullName(orgTreeInfo1.getFirstName() + " " + orgTreeInfo1.getLastName());
                } else
                    throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, null,
                            messageSource.getMessage("exception.evaluation.assessor.not.found", new Object[]{evaluationCreate.getPostCode()}, LocaleContextHolder.getLocale()));

            } else if (Objects.isNull(orgTreeInfo.getNationalCodeParent()) && Objects.isNull(orgTreeInfo.getPostPath())) {
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, null,
                        messageSource.getMessage("exception.evaluation.national-code.not.found", new Object[]{evaluationCreate.getPostCode()}, LocaleContextHolder.getLocale()));
            } else {
                evaluation.setAssessorPostCode(orgTreeInfo.getPostParentCode());
                evaluation.setAssessorNationalCode(orgTreeInfo.getNationalCodeParent());
                evaluation.setAssessorFullName(orgTreeInfo.getFirstNameParent() + " " + orgTreeInfo.getLastNameParent());
            }
            evaluation.setAssessFullName(orgTreeInfo.getFullName());
            evaluation.setAssessNationalCode(orgTreeInfo.getNationalCode());
            evaluation.setAssessPostCode(evaluationCreate.getPostCode());
            Long methodCatalogId = methodTypes.stream().filter(x -> x.getCode().equals("Organizational-chart")).findFirst().orElseThrow().getId();
            evaluation.setMethodCatalogId(methodCatalogId);
        }
    }

    private void revokedSpecialCase(List<Object> specialCaseRevoked) {
        if (!specialCaseRevoked.isEmpty()) {
            BatchDTO.Create batchDTO = new BatchDTO.Create();
            batchDTO.setInputDetails(specialCaseRevoked);
            Catalog titleCatalog = catalogRepository.findByCode(BATCH_CREATE_CHANGE_STATUS_SPECIAL_CASE).orElse(null);
            batchDTO.setTitleCatalogId(Objects.nonNull(titleCatalog) ? titleCatalog.getId() : null);

            batchService.create(batchDTO);
        }
    }

    private SpecialCaseDTO.Info getSpecialCaseInfo(EvaluationDTO.CreateList evaluationCreate, List<SpecialCaseDTO.Info>
            byAssessNationalCode, List<SpecialCaseDTO.Info> specialCaseInfos, List<Object> specialCaseRevoked) {

        if (!byAssessNationalCode.isEmpty()) {
            byAssessNationalCode.forEach(specialCase -> {
                if (specialCase.getAssessPostCode().equals(evaluationCreate.getPostCode()))
                    specialCaseInfos.add(specialCase);
                else
                    specialCaseRevoked.add(specialCase);
            });
            if (!specialCaseInfos.isEmpty())
                return specialCaseInfos.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION')")
    public EvaluationDTO.Info update(Long id, EvaluationDTO.Update dto) {
        Evaluation evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(evaluation, dto);
        Evaluation save = repository.save(evaluation);
        Post assessPost = postService.getByPostCode(evaluation.getAssessPostCode());
        return mapper.entityToDtoInfo(save, assessPost);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION')")
    public void delete(Long id) {
        Evaluation evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, entity -> mapper.entityToDtoInfo(entity, postService.getByPostCode(entity.getAssessPostCode())), request);
    }

    @Override
    @Transactional
    public EvaluationDTO.ErrorResponseDTO changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO) {
        EvaluationDTO.ErrorResponseDTO errorResponseDTO = new EvaluationDTO.ErrorResponseDTO();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            List<Long> ids = changeStatusDTO.getEvaluationIds();
            List<String> invalidPostList = new ArrayList<>();
            List<Long> invalidIdList = new ArrayList<>();
            StringBuilder errorMessage = new StringBuilder();
            for (Long id : ids) {
                Optional<Evaluation> optionalEvaluation = repository.findById(id);
                if (optionalEvaluation.isPresent()) {
                    Evaluation evaluation = optionalEvaluation.get();
                    String miDate = DateUtil.convertKhToMi1(evaluation.getEvaluationPeriod().getEndDate());
                    Date evaluationDate = new SimpleDateFormat("yyyy-MM-dd").parse(miDate);
                    EvaluationPeriodDTO.Info evaluationPeriod = evaluationPeriodService.get(evaluation.getEvaluationPeriodId());
                    Boolean validatePosts = Boolean.TRUE;

                    if (evaluation.getStatusCatalog().getCode() != null && !Objects.equals(evaluation.getStatusCatalog().getCode(), FINALIZED)
                            && !Objects.equals(changeStatusDTO.getStatus().toLowerCase(Locale.ROOT), "previous"))
                        validatePosts = validatePosts(Collections.singletonList(id));

                    if (Boolean.TRUE.equals(validatePosts)) {
                        if (evaluationDate != null && evaluationDate.after(new Date())) {
                            switch (changeStatusDTO.getStatus().toLowerCase(Locale.ROOT)) {
                                case "next":
                                    if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(INITIAL)) {
                                        if (DateTimeComparator.getDateOnlyInstance().compare(evaluationPeriod.getValidationStartDate(), new Date()) > 0 ||
                                                DateTimeComparator.getDateOnlyInstance().compare(evaluationPeriod.getValidationEndDate(), new Date()) < 0) {
                                            errorMessage.append(messageSource.getMessage("exception.changing.the.status.to.validated.is.only.possible.in.the.range", null, LocaleContextHolder.getLocale()));
                                        } else
                                            createEvaluationItems(evaluation);
                                    } else if (Objects.nonNull(evaluation.getStatusCatalog().getCode()) && evaluation.getStatusCatalog().getCode().equals(VALIDATED)) {
                                        // توسط جاب شبانه انجام میشود
                                    } else if (Objects.nonNull(evaluation.getStatusCatalog().getCode()) && evaluation.getStatusCatalog().getCode().equals(AWAITING)) {
                                        Optional<Catalog> optionalCatalog = catalogRepository.findByCode(FINALIZED);
                                        optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                        repository.save(evaluation);
                                    }
                                    break;
                                case "previous":
                                    if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(FINALIZED)) {
                                        Optional<Catalog> optionalCatalog = catalogRepository.findByCode(AWAITING);
                                        optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                        repository.save(evaluation);

                                    } else if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(AWAITING)) {
                                        Optional<Catalog> optionalCatalog = catalogRepository.findByCode(VALIDATED);
                                        optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                        evaluation.setAverageScore(null);
                                        repository.save(evaluation);
                                    } else if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(VALIDATED)) {
                                        Optional<Catalog> optionalCatalog = catalogRepository.findByCode(INITIAL);
                                        optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                        deleteItems(evaluation);
                                        evaluation.setAverageScore(null);
                                        repository.save(evaluation);
                                    }
                                    break;
                            }
                        } else {
                            errorMessage.append(messageSource.getMessage("exception.evaluation.date", null, locale));
                        }
                    } else {
                        invalidPostList.add(evaluation.getEvaluationPeriod().getTitle());
                        invalidIdList.add(id);
                    }
                }
            }
            if (errorMessage.isEmpty()) {
                if (invalidIdList.isEmpty()) {
                    errorResponseDTO.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
                    errorResponseDTO.setStatus(200);
                } else {

                    errorResponseDTO.setMessage(messageSource.getMessage("exception.period.has.invalid.posts", new Object[]{invalidPostList}, null, locale));
                    errorResponseDTO.setEvaluationIds(invalidIdList);
                    errorResponseDTO.setStatus(406);
                }
            } else {
                errorResponseDTO.setMessage(String.valueOf(errorMessage));
                errorResponseDTO.setStatus(406);
            }
            return errorResponseDTO;
        } catch (Exception e) {
            errorResponseDTO.setMessage(e.getMessage());
            errorResponseDTO.setStatus(EvaluationHandleException.ErrorType.EvaluationDeadline.getHttpStatusCode());
            return errorResponseDTO;
        }
    }

    @Override
    @Transactional
    public Boolean validatePosts(List<Long> evaluationIds) {

        List<String> postCodes = repository.findAllByIdIn(evaluationIds).stream().map(Evaluation::getAssessPostCode).toList();
        if (postCodes.isEmpty())
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, null,
                    messageSource.getMessage("exception.evaluation.period.has.not.any.post", null, LocaleContextHolder.getLocale()));

        List<PostDTO.Info> allPostsHasNotGrade = postService.getPostGradeHasNotGroupByPostCodes(postCodes);
        if (!allPostsHasNotGrade.isEmpty())
            return false;

        List<PostDTO.Info> groupHasNotGroupTypeByPostCode = postService.getGroupHasNotGroupTypeByPostCodes(postCodes);
        if (!groupHasNotGroupTypeByPostCode.isEmpty())
            return false;

        List<GroupTypeDTO.Info> allGroupTypeByGroupPostCode = groupTypeService.getAllByPostCodes(postCodes);
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

    @Override
    @Transactional(readOnly = true)
    public EvaluationDTO.SpecResponse getAllByAssessNationalCodeAndStatusCatalogId(String assessNationalCode, Long statusCatalogId, int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Evaluation> evaluationPageList = repository.findAllByAssessNationalCodeAndStatusCatalogId(assessNationalCode, statusCatalogId, pageable);
        List<EvaluationDTO.EvaluationPeriodDashboard> evaluationInfos = mapper.entityToDtoEvaluationPeriodDashboardList(evaluationPageList.getContent());

        EvaluationDTO.Response response = new EvaluationDTO.Response();
        EvaluationDTO.SpecResponse specResponse = new EvaluationDTO.SpecResponse();

        if (evaluationInfos != null) {
            response.setData(evaluationInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count > evaluationPageList.getTotalElements() ? (int) evaluationPageList.getTotalElements() : startIndex + count)
                    .setTotalRows((int) evaluationPageList.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationDTO.EvaluationAverageScoreData getEvaluationAverageScoreDataByAssessNationalCodeAndEvaluationPeriodId(String assessNationalCode, Long evaluationPeriodId) {
        Optional<Evaluation> optionalEvaluation = repository.findFirstByAssessNationalCodeAndEvaluationPeriodId(assessNationalCode, evaluationPeriodId);
        return mapper.entityToDtoAverageScoreData(optionalEvaluation.orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound)));
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationDTO.EvaluationAverageScoreData getEvaluationAverageScoreDataByAssessorNationalCodeAndEvaluationPeriodId(String assessorNationalCode, Long evaluationPeriodId) {
        List<Evaluation> evaluationList = repository.findByAssessorNationalCodeAndEvaluationPeriodId(assessorNationalCode, evaluationPeriodId);
        return calculateAverageScore(evaluationList);
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationDTO.EvaluationAverageScoreData getSubEvaluationAverageScoreDataByAssessorNationalCodeAndEvaluationPeriodId(Long evaluationPeriodId) {
        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq evaluationPeriodCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.equals)
                .setFieldName("evaluationPeriodId")
                .setValue(evaluationPeriodId);
        criteriaRqList.add(evaluationPeriodCriteriaRq);
        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        SearchDTO.SearchRq request = new SearchDTO.SearchRq();
        request.setCriteria(criteriaRq);
        List<EvaluationViewDTO.Info> infoList = evaluationViewService.searchEvaluationComprehensive(request, Integer.MAX_VALUE, 0, null).getList();
        List<Evaluation> evaluationList = mapper.viewDtoToEntityList(infoList);
        return calculateAverageScore(evaluationList);
    }

    private EvaluationDTO.EvaluationAverageScoreData calculateAverageScore(List<Evaluation> evaluationList) {
        EvaluationDTO.EvaluationAverageScoreData evaluationAverageScoreData = new EvaluationDTO.EvaluationAverageScoreData();
        List<Long> evaluationIds = evaluationList.stream().map(Evaluation::getId).toList();
        List<EvaluationItemDTO.GroupTypeAverageScoreDto> averageScoreByEvaluationIds = evaluationItemService.getAllGroupTypeAverageScoreByEvaluationIds(evaluationIds);
        List<EvaluationItemDTO.GroupTypeAverageScoreDto> behavioralAverageScoreDto = averageScoreByEvaluationIds.stream().filter(q -> Objects.nonNull(q.getKpiTitle())
                && q.getKpiTitle().equals(KPI_TYPE_TITLE_BEHAVIORAL)).toList();
        behavioralAverageScoreDto.stream().findFirst().ifPresent(q ->
                evaluationAverageScoreData.setBehavioralAverageScore(q.getAverageScore() / evaluationIds.size())
        );
        List<EvaluationItemDTO.GroupTypeAverageScoreDto> developmentAverageScoreDto = averageScoreByEvaluationIds.stream().filter(q -> Objects.nonNull(q.getKpiTitle())
                && q.getKpiTitle().equals(KPI_TYPE_TITLE_DEVELOPMENT)).toList();
        developmentAverageScoreDto.stream().findFirst().ifPresent(q ->
                evaluationAverageScoreData.setDevelopmentAverageScore(q.getAverageScore() / evaluationIds.size())
        );
        List<String> assessPostCodes = evaluationList.stream().map(Evaluation::getAssessPostCode).toList().stream().map(s -> Arrays.stream("/".split(s)).findFirst().orElseThrow()).toList();
        groupTypeService.getTypeByAssessPostCode(assessPostCodes, LEVEL_DEF_POST).stream().findFirst().ifPresent(q -> {
            List<EvaluationItemDTO.PostMeritTupleDTO> postMeritTupleDTOList = evaluationItemService.getAllPostMeritByEvalId(evaluationIds);
            double operationalAverageScore = postMeritTupleDTOList.stream().mapToDouble(EvaluationItemDTO.PostMeritTupleDTO::getQuestionnaireAnswerCatalogValue).sum() * 100 / q.getWeight();
            evaluationAverageScoreData.setOperationalAverageScore((long) operationalAverageScore / evaluationIds.size());
        });
        if (!evaluationList.isEmpty()) {
            long averageScore = evaluationList.stream().mapToLong(Evaluation::getAverageScore).sum() / evaluationList.size();
            evaluationAverageScoreData.setAverageScore(averageScore);
        }
        return evaluationAverageScoreData;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO.MostParticipationInFinalizedEvaluation> mostParticipationInFinalizedEvaluationPerOmoor(Long evaluationPeriodId, Long finalizedStatusCatalogId) {
        return repository.mostParticipationInFinalizedEvaluationPerOmoor(evaluationPeriodId, finalizedStatusCatalogId);
    }

    @Scheduled(cron = "0 30 0 * * *")
    @Transactional
    public void automateChangeStatus() {
        String message = messageSource.getMessage(
                "exception.evaluation.is.finished.because.competency.elements.and.system.rules.were.not.set.properly",
                null, LocaleContextHolder.getLocale());
        repository.updateEvaluationStatusId(DateUtil.todayDate(), message);
    }

    private void deleteItems(Evaluation evaluation) {
        List<Long> itemIds = evaluationItemService.getByEvalId(evaluation.getId()).stream().map(EvaluationItemDTO.Info::getId).toList();
        evaluationItemService.deleteAll(itemIds);
    }

    public void createEvaluationItems(Evaluation evaluation) {
        List<EvaluationItemDTO.Create> requests = new ArrayList<>();
        String assessPostCode = evaluation.getAssessPostCode().substring(0, evaluation.getAssessPostCode().indexOf("/"));
        EvaluationItemDTO.CreateInfo createInfo = new EvaluationItemDTO.CreateInfo();
        createInfo.setAssessPostCode(assessPostCode);
        createInfo.setEvaluationId(evaluation.getId());
        createInfo.setStatusCatalogId(evaluation.getStatusCatalogId());
        List<EvaluationItemDTO.CreateItemInfo> infoByAssessPostCodeForCreate =
                evaluationItemService.getInfoByAssessPostCodeForCreate(createInfo);
        infoByAssessPostCodeForCreate.forEach(info -> {
            List<EvaluationItemDTO.MeritTupleDTO> items = info.getMeritTuple();
            items.forEach(item -> {
                EvaluationItemDTO.Create evaluationItemDTO = new EvaluationItemDTO.Create();
                evaluationItemDTO.setEvaluationId(evaluation.getId());
                List<Long> instances = item.getInstances().stream().map(EvaluationItemDTO.InstanceTupleDTO::getId).toList();
                evaluationItemDTO.setInstanceIds(instances);
                MeritComponentDTO.Info lastActiveByMeritComponent = meritComponentService.findLastActiveByMeritComponentId(item.getMeritComponent().getId());
                evaluationItemDTO.setMeritRev(lastActiveByMeritComponent.getRev());
                evaluationItemDTO.setMeritId(lastActiveByMeritComponent.getId());
                evaluationItemDTO.setGroupTypeMeritId(item.getGroupTypeMeritId());
                evaluationItemDTO.setPostMeritComponentId(item.getPostMeritId());
                evaluationItemDTO.setInstanceGroupTypeMerits(item.getInstanceGroupTypeMerits());
                evaluationItemDTO.setPostMeritInstanceList(item.getPostMeritInstanceList());
                requests.add(evaluationItemDTO);
            });
        });
        evaluationItemService.createAll(requests);
    }

    @Override
    public List<String> sendNotification() {

        List<String> notificationList = new ArrayList<>();
        Catalog catalog = catalogRepository.findByCode(INITIAL).orElseThrow();
        List<EvaluationPeriodDTO.Info> allByCreatorAndStartDateValidation = evaluationPeriodService.
                getAllByCreatorAndStartDateValidation(SecurityUtil.getUsername(), DateUtil.todayDate(), catalog.getId());
        if (!allByCreatorAndStartDateValidation.isEmpty()) {
            List<String> periodTitleList = allByCreatorAndStartDateValidation.stream().map(EvaluationPeriodDTO::getTitle).toList();
            notificationList.add(messageSource.getMessage("exception.period.validation.time.has.started", new Object[]{periodTitleList}, LocaleContextHolder.getLocale()));
            log.info("=========>  " + messageSource.getMessage("exception.period.validation.time.has.started", new Object[]{periodTitleList}, LocaleContextHolder.getLocale()));
        }
        List<EvaluationPeriodDTO.RemainDate> allByCreatorAndRemainDateToEndDateValidation = evaluationPeriodService.
                getAllByCreatorAndRemainDateToEndDateValidation(SecurityUtil.getUsername(), DateUtil.todayDate(), catalog.getId());
        if (!allByCreatorAndRemainDateToEndDateValidation.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            allByCreatorAndRemainDateToEndDateValidation.forEach(data ->
                    messageBuilder.append(data.getPeriodTitle()).append(" ").append(data.getRemainDate()).append(" روز ,")
            );
            StringBuilder msg = messageBuilder.replace(messageBuilder.length() - 1, messageBuilder.length(), " ");
            notificationList.add(messageSource.getMessage("exception.period.remaining.time.for.the.validation.of.the.course", new Object[]{msg}, LocaleContextHolder.getLocale()));
            log.info("=================>  " + messageSource.getMessage("exception.period.remaining.time.for.the.validation.of.the.course", new Object[]{msg}, LocaleContextHolder.getLocale()));
        }
        List<EvaluationPeriodDTO.Info> allByAssessorAndStartDateAssessment =
                evaluationPeriodService.getAllByAssessorAndStartDateAssessment(SecurityUtil.getNationalCode(), DateUtil.todayDate());
        if (!allByAssessorAndStartDateAssessment.isEmpty()) {
            notificationList.add(messageSource.getMessage("exception.period.time.to.complete.your.assessments.has.begun", null, LocaleContextHolder.getLocale()));
            log.info("=================>  " + messageSource.getMessage("exception.period.time.to.complete.your.assessments.has.begun", null, LocaleContextHolder.getLocale()));
        }
        Catalog catalog1 = catalogRepository.findByCode(AWAITING).orElseThrow();
        List<EvaluationPeriodDTO.RemainDate> allByAssessorAndStartDateAssessmentAndStatusId =
                evaluationPeriodService.getAllByAssessorAndStartDateAssessmentAndStatusId(SecurityUtil.getNationalCode(), DateUtil.todayDate(), catalog1.getId());
        if (!allByAssessorAndStartDateAssessmentAndStatusId.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            allByAssessorAndStartDateAssessmentAndStatusId.forEach(data ->
                    messageBuilder.append(data.getPeriodTitle()).append(" ").append(data.getRemainDate()).append(" روز ,"));
            StringBuilder msg = messageBuilder.replace(messageBuilder.length() - 1, messageBuilder.length(), " ");
            notificationList.add(messageSource.getMessage("exception.period.that.are.incomplete.sent.in.the.final.state", new Object[]{msg}, LocaleContextHolder.getLocale()));
            log.info("=================>  " + messageSource.getMessage("exception.period.that.are.incomplete.sent.in.the.final.state", new Object[]{msg}, LocaleContextHolder.getLocale()));
        }

        return notificationList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO.AverageWeightDTO> getFinalizedAverageByGradeAndPeriodEvaluation(Long periodId) {
        String omoorCode = repository.getOmoorCodeByAssessNationalCodeAndPeriodId(SecurityUtil.getNationalCode(), periodId);
        return repository.getFinalizedAverageByGradeAndPeriodEvaluation(periodId, Collections.singletonList(omoorCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO.BestAssessAverageScoreDTO> getBestAssessesByAssessorAndPeriodEvaluation(int count, int startIndex, Long periodId) {
        List<String> omoorCode = repository.getOmoorCodeByAssessorNationalCodeAndPeriodId(SecurityUtil.getNationalCode(), periodId);
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        return repository.getBestAssessesByOmoor(periodId, omoorCode, pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationDTO.BestAssessAverageScoreDTO> getBestAssessesByOmoor(int count, int startIndex, Long periodId) {
        String omoorCode = repository.getOmoorCodeByAssessNationalCodeAndPeriodId(SecurityUtil.getNationalCode(), periodId);
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        return repository.getBestAssessesByOmoor(periodId, Collections.singletonList(omoorCode), pageable.getPageNumber(), pageable.getPageSize());
    }

    @SneakyThrows
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('R_EVALUATION_PERIOD')")
    public ResponseEntity<byte[]> downloadInvalidPostExcel(List<Long> evaluationIds) {

        List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostList = createInvalidPostList(evaluationIds);
        byte[] body = BaseService.exportExcelByList(invalidPostList, null, "گزارش لیست پست ها");
        ExcelGenerator.ExcelDownload excelDownload = new ExcelGenerator.ExcelDownload(body);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                .body(excelDownload.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNumberOfAssessorWorkInWorkSpace() {
        String nationalCode = SecurityUtil.getNationalCode();
        return repository.getNumberOfAssessorWorkInWorkSpace(catalogService.getByCode("Awaiting-review").getId(), nationalCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getAssessorWorkInWorkSpace() {
        String nationalCode = SecurityUtil.getNationalCode();
        return repository.getAssessorWorkInWorkSpace(catalogService.getByCode("Awaiting-review").getId(), nationalCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNumberOfAssessorWorkInWorkSpaceNotification(String token) {
        String nationalCode;
        String url = oAuthUrl + "/tokens/" + token;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, null);
        try {
            OAuthCurrentUserDTO oAuthCurrentUserDTO = restTemplateOAuth.exchange(url, HttpMethod.GET, entity, OAuthCurrentUserDTO.class).getBody();
            if (oAuthCurrentUserDTO != null) {
                nationalCode = oAuthCurrentUserDTO.getPrincipal().getNationalCode();
                if (nationalCode != null)
                    return repository.getNumberOfAssessorWorkInWorkSpace(catalogService.getByCode("Awaiting-review").getId(), nationalCode);
                else
                    throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "شخص مورد نظر در سیستم OAuth یافت نشد");
            } else
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "شخص مورد نظر در سیستم OAuth یافت نشد");
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.Unauthorized, "خطا در دسترسی به سیستم OAuth");
        }
    }

    private List<EvaluationPeriodPostDTO.InvalidPostExcel> createInvalidPostList(List<Long> evaluationIds) {
        List<String> postCodes = repository.findAllByIdIn(evaluationIds).stream().map(Evaluation::getAssessPostCode).toList();
        List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList = new ArrayList<>();
        validateGradeHasGroup(postCodes, invalidPostExcelList);
        validateGroupGradeHasNotGroupType(postCodes, invalidPostExcelList);

        List<GroupTypeDTO.Info> allGroupTypeByPostCode = groupTypeService.getAllByPostCodes(postCodes);
        Map<Long, List<GroupTypeDTO.Info>> allGroupTypeMap = allGroupTypeByPostCode.stream().collect(Collectors.groupingBy(GroupTypeDTO::getGroupId));
        validateAllGroupTypeMeritPost(postCodes, invalidPostExcelList, allGroupTypeMap);
        return invalidPostExcelList;
    }

    private void validateAllGroupTypeMeritPost(List<String> postCodes, List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList, Map<Long, List<GroupTypeDTO.Info>> allGroupTypeMap) {
        int typeSize = kpiTypeService.findAll().size();

        Long statusCatalogId = catalogService.getByCode(REVOKED_MERIT).getId();
        allGroupTypeMap.forEach((groupId, groupTypes) -> {
            /*check all kpiType define for these group*/
            validateAllGroupType(invalidPostExcelList, typeSize, groupTypes, groupTypes.size(), "exception.not.define.all.group-type.for.these.group");
            long totalWeight = groupTypes.stream().mapToLong(GroupTypeDTO::getWeight).sum();
            /*check totalWeight is 100*/
            validateAllGroupType(invalidPostExcelList, 100, groupTypes, totalWeight, "exception.invalid.group.type.weight.for.these.group");

            groupTypes.forEach(groupType -> {
                if (!groupType.getKpiType().getTitle().equals(EvaluationConstant.KPI_TYPE_TITLE_OPERATIONAL)) {
                    List<GroupTypeMeritDTO.Info> allGroupTypeMeritByGroupType = groupTypeMeritService.getAllByGroupTypeIdAndMeritStatusId(groupType.getId(), statusCatalogId);
                    /*check define meritComponents for all groupTypes that are Behavioral or Development*/
                    validateGroupTypeMeritIsDefine(invalidPostExcelList, groupTypes, allGroupTypeMeritByGroupType);
                    /*check total groupTypeMerit's weight for all groupTypes that are Behavioral or Development*/
                    validateGroupTypeMeritWeight(invalidPostExcelList, groupTypes, groupType, allGroupTypeMeritByGroupType);
                }
            });
        });
        postCodes.forEach(postCode -> {
            List<EvaluationItemDTO.MeritTupleDTO> byPostCode = postMeritComponentService.getByPostCodeAndMeritStatus(postCode, statusCatalogId);
            if (byPostCode.isEmpty())
                validatePostMeritIsDefine(invalidPostExcelList, postCode);

            long totalWeightPostMerit = byPostCode.stream().mapToLong(EvaluationItemDTO.MeritTupleDTO::getWeight).sum();
            if (totalWeightPostMerit != 100) {
                EvaluationPeriodPostDTO.InvalidPostExcel notDefinePostMerit = new EvaluationPeriodPostDTO.InvalidPostExcel();
                String errorMessage = messageSource.getMessage("exception.invalid.post-merit.total.weight",
                        new Object[]{postCode}, LocaleContextHolder.getLocale());
                notDefinePostMerit.setDescription(errorMessage);
                notDefinePostMerit.setGroupPostCode(String.valueOf(postCode));
                Post post = postService.getByPostCode(postCode);
                notDefinePostMerit.setPostTitle(post.getPostTitle());
                notDefinePostMerit.setPostGradeTitle(post.getPostGradeTitle());
                notDefinePostMerit.setTotalWeightOperational(totalWeightPostMerit);
                invalidPostExcelList.add(notDefinePostMerit);
            }
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
        Post post = postService.getByPostCode(groupPostCode);
        notDefinePostMerit.setPostTitle(post.getPostTitle());
        notDefinePostMerit.setPostGradeTitle(post.getPostGradeTitle());
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
            inValidTotalGroupTypeMeritWeight.setTotalWeightGroup(groupTypes.stream().mapToLong(GroupTypeDTO::getWeight).sum());
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
            notDefineMerit.setTotalWeightGroup(groupTypes.stream().mapToLong(GroupTypeDTO::getWeight).sum());
            notDefineMerit.setPostGradeTitle(String.valueOf(groupTypes.get(0).getGroup().getGrade().stream().map(GradeDTO::getTitle).toList()));
            invalidPostExcelList.add(notDefineMerit);
        }
    }

    private void validateAllGroupType(List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList, int typeSize, List<GroupTypeDTO.Info> groupTypes, long weight, String s) {
        if (weight != typeSize) {
            EvaluationPeriodPostDTO.InvalidPostExcel notDefineAllType = new EvaluationPeriodPostDTO.InvalidPostExcel();
            String errorMessage = messageSource.getMessage(s,
                    new Object[]{groupTypes.get(0).getGroup().getTitle()}, LocaleContextHolder.getLocale());
            notDefineAllType.setDescription(errorMessage);
            notDefineAllType.setGroupTypeTitle(groupTypes.get(0).getGroup().getTitle());
            notDefineAllType.setTotalWeightGroup(groupTypes.stream().mapToLong(GroupTypeDTO::getWeight).sum());
            notDefineAllType.setPostGradeTitle(String.valueOf(groupTypes.get(0).getGroup().getGrade().stream().map(GradeDTO::getTitle).toList()));
            invalidPostExcelList.add(notDefineAllType);
        }
    }

    private void validateGroupGradeHasNotGroupType(List<String> postCodes, List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList) {
        List<PostDTO.Info> groupHasNotGroupTypeByPostCode = postService.getGroupHasNotGroupTypeByPostCodes(postCodes);
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

    private void validateGradeHasGroup(List<String> postCodes, List<EvaluationPeriodPostDTO.InvalidPostExcel> invalidPostExcelList) {
        List<PostDTO.Info> allPostGradeHasNotGroup = postService.getPostGradeHasNotGroupByPostCodes(postCodes);
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
}

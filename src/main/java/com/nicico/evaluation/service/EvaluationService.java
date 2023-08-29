package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
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
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.nicico.evaluation.utility.EvaluationConstant.*;

@RequiredArgsConstructor
@Service
public class EvaluationService implements IEvaluationService {

    private final EvaluationMapper mapper;
    private final IPostService postService;
    private final IBatchService batchService;
    private final PageableMapper pageableMapper;
    private final EvaluationRepository repository;
    private final CatalogRepository catalogRepository;
    private IEvaluationItemService evaluationItemService;
    private final ISpecialCaseService specialCaseService;
    private final ResourceBundleMessageSource messageSource;
    private final IOrganizationTreeService organizationTreeService;
    private final IMeritComponentService meritComponentService;
    private IEvaluationPeriodService evaluationPeriodService;

    @Autowired
    public void setEvaluationItemService(@Lazy IEvaluationItemService evaluationItemService) {
        this.evaluationItemService = evaluationItemService;
    }

    @Autowired
    public void setEvaluationPeriodService(@Lazy IEvaluationPeriodService evaluationPeriodService) {
        this.evaluationPeriodService = evaluationPeriodService;
    }

    @Override
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست آیتم های ارزیابی ها");
        return new ExcelGenerator.ExcelDownload(body);
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
            evaluation.setAssessorPostCode(orgTreeInfo.getPostParentCode());
            evaluation.setAssessorNationalCode(orgTreeInfo.getNationalCodeParent());
            evaluation.setAssessorFullName(orgTreeInfo.getFirstNameParent() + " " + orgTreeInfo.getLastNameParent());
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
    public BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO) {
        BaseResponse response = new BaseResponse();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            List<Long> ids = changeStatusDTO.getEvaluationIds();
            StringBuilder errorMessage = new StringBuilder();
            for (Long id : ids) {
                Optional<Evaluation> optionalEvaluation = repository.findById(id);
                if (optionalEvaluation.isPresent()) {
                    Evaluation evaluation = optionalEvaluation.get();
                    String miDate = DateUtil.convertKhToMi1(evaluation.getEvaluationPeriod().getEndDate());
                    Date evaluationDate = new SimpleDateFormat("yyyy-MM-dd").parse(miDate);
                    EvaluationPeriodDTO.Info evaluationPeriod = evaluationPeriodService.get(evaluation.getEvaluationPeriodId());

                    if (evaluationDate != null && evaluationDate.after(new Date())) {
                        switch (changeStatusDTO.getStatus().toLowerCase(Locale.ROOT)) {
                            case "next":
                                if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(INITIAL)) {
                                    if (evaluationPeriod.getValidationStartDate().after(new Date()) || evaluationPeriod.getValidationEndDate().before(new Date())) {
                                        errorMessage.append(messageSource.getMessage("exception.changing.the.status.to.validated.is.only.possible.in.the.range", null, LocaleContextHolder.getLocale()));
                                        break;
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

                }
            }
            if (errorMessage.isEmpty()) {
                response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
                response.setStatus(200);
            } else {
                response.setMessage(String.valueOf(errorMessage));
                response.setStatus(406);
            }
            return response;
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(EvaluationHandleException.ErrorType.EvaluationDeadline.getHttpStatusCode());
            return response;
        }
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

    private void createEvaluationItems(Evaluation evaluation) {
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

}

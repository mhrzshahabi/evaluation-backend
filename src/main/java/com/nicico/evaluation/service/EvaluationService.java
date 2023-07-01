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
    private final EvaluationRepository repository;
    private final PageableMapper pageableMapper;
    private final CatalogRepository catalogRepository;
    private final ResourceBundleMessageSource messageSource;
    private final ISpecialCaseService specialCaseService;
    private final IOrganizationTreeService organizationTreeService;
    private IEvaluationItemService evaluationItemService;
    private final IBatchService batchService;

    @Autowired
    public void setEvaluationItemService(@Lazy IEvaluationItemService evaluationItemService) {
        this.evaluationItemService = evaluationItemService;
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
        Page<Evaluation> Evaluations = repository.findAll(pageable);
        List<EvaluationDTO.Info> EvaluationInfos = mapper.entityToDtoInfoList(Evaluations.getContent());

        EvaluationDTO.Response response = new EvaluationDTO.Response();
        EvaluationDTO.SpecResponse specResponse = new EvaluationDTO.SpecResponse();

        if (EvaluationInfos != null) {
            response.setData(EvaluationInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) Evaluations.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.Info get(Long id) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(Evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public EvaluationDTO.Info getAllByPeriodIdAndAssessPostCode(Long periodId, String assessPostCode) {
        Evaluation evaluation = repository.findByEvaluationPeriodIdAndAssessRealPostCode(periodId, assessPostCode);
        return mapper.entityToDtoInfo(evaluation);
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
        Evaluation Evaluation = mapper.dtoCreateToEntity(dto);
        Evaluation = repository.save(Evaluation);
        return mapper.entityToDtoInfo(Evaluation);
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
            evaluation.setStatusCatalogId(catalogStatus.getId());
            evaluation.setEvaluationPeriodId(evaluationCreate.getEvaluationPeriodId());
            OrganizationTreeDTO.InfoTree orgTreeInfo = organizationTreeService.getByPostCode(evaluationCreate.getPostCode());
            List<Evaluation> evaluationList =
                    repository.findByEvaluationPeriodIdAndAssessPostCode(evaluationCreate.getEvaluationPeriodId(), evaluationCreate.getPostCode());
            if (!evaluationList.isEmpty())
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);

            validateSpecialCase(methodTypes, specialCaseInfos, specialCaseRevoked, evaluationCreate, evaluation, orgTreeInfo);

            evaluation = repository.save(evaluation);
            evaluationInfo.add(mapper.entityToDtoInfo(evaluation));
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
        if (scInfo != null) {
            evaluation.setAssessorPostCode(scInfo.getAssessorPostCode());
            evaluation.setAssessorNationalCode(scInfo.getAssessorNationalCode());
            evaluation.setAssessorFullName(scInfo.getAssessorFullName());
            evaluation.setAssessFullName(scInfo.getAssessFullName());
            evaluation.setAssessNationalCode(scInfo.getAssessNationalCode());
            evaluation.setAssessPostCode(evaluationCreate.getPostCode());
            evaluation.setAssessRealPostCode(scInfo.getAssessRealPostCode());
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
            evaluation.setAssessRealPostCode(evaluationCreate.getPostCode());
            Long methodCatalogId = methodTypes.stream().filter(x -> x.getCode().equals("Organizational-chart")).findFirst().orElseThrow().getId();
            evaluation.setMethodCatalogId(methodCatalogId);
        }
    }

    private void revokedSpecialCase(List<Object> specialCaseRevoked) {
        if (!specialCaseRevoked.isEmpty()) {
            BatchDTO.Create batchDTO = new BatchDTO.Create();
            batchDTO.setInputDetails(specialCaseRevoked);
            Catalog titleCatalog = catalogRepository.findByCode(BATCHCREATE_CHANGESTATUS_SPECIALCASE).orElse(null);
            batchDTO.setTitleCatalogId(Objects.nonNull(titleCatalog) ? titleCatalog.getId() : null);

            batchService.create(batchDTO);
        }
    }

    private SpecialCaseDTO.Info getSpecialCaseInfo(EvaluationDTO.CreateList evaluationCreate, List<SpecialCaseDTO.Info>
            byAssessNationalCode, List<SpecialCaseDTO.Info> specialCaseInfos, List<Object> specialCaseRevoked) {

        if (!byAssessNationalCode.isEmpty()) {
            byAssessNationalCode.forEach(specialCase -> {
                if (specialCase.getAssessRealPostCode().equals(evaluationCreate.getPostCode()))
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
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(Evaluation, dto);
        Evaluation save = repository.save(Evaluation);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION')")
    public EvaluationDTO.Info update(Long id, Evaluation evaluation) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        EvaluationDTO.Update evaluationDTO = mapper.entityToUpdateDto(evaluation);
        mapper.update(Evaluation, evaluationDTO);
        Evaluation save = repository.save(Evaluation);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION')")
    public void delete(Long id) {
        Evaluation Evaluation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(Evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION')")
    public SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional
    public BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO) {
        BaseResponse response = new BaseResponse();
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            List<Long> ids = changeStatusDTO.getEvaluationIds();
            for (Long id : ids) {
                Optional<Evaluation> optionalEvaluation = repository.findById(id);
                if (optionalEvaluation.isPresent()) {
                    Evaluation evaluation = optionalEvaluation.get();
                    String miDate = DateUtil.convertKhToMi1(evaluation.getEvaluationPeriod().getEndDate());
                    Date evaluationDate = new SimpleDateFormat("yyyy-MM-dd").parse(miDate);
                    if (evaluationDate != null && evaluationDate.after(new Date())) {
                        switch (changeStatusDTO.getStatus().toLowerCase(Locale.ROOT)) {
                            case "next" -> {
                                if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(INITIAL)) {
                                    createEvaluationItems(evaluation);

                                } else if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(AWAITING)) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode(FINALIZED);
                                    optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                    repository.save(evaluation);
                                }
                            }
                            case "previous" -> {
                                if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(FINALIZED)) {
                                    updateEvaluationItems(evaluation);

                                } else if (evaluation.getStatusCatalog().getCode() != null && evaluation.getStatusCatalog().getCode().equals(AWAITING)) {
                                    Optional<Catalog> optionalCatalog = catalogRepository.findByCode(INITIAL);
                                    optionalCatalog.ifPresent(catalog -> evaluation.setStatusCatalogId(catalog.getId()));
                                    deleteItems(evaluation);
                                    repository.save(evaluation);
                                }
                            }
                        }
                    } else {
                        response.setStatus(406);
                        response.setMessage(messageSource.getMessage("exception.evaluation.date", null, locale));
                        return response;
                    }
                }
            }
            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
            response.setStatus(200);
            return response;
        } catch (Exception e) {
            response.setMessage(messageSource.getMessage("exception.un-managed", null, locale));
            response.setStatus(EvaluationHandleException.ErrorType.EvaluationDeadline.getHttpStatusCode());
            return response;
        }
    }

    private void deleteItems(Evaluation evaluation) {
        List<Long> itemIds = evaluationItemService.getByEvalId(evaluation.getId()).stream().map(EvaluationItemDTO.Info::getId).toList();
        evaluationItemService.deleteAll(itemIds);
    }

    @Override
    public List<String> getUsedPostInEvaluation(Long evaluationPeriodId) {
        return repository.getUsedPostInEvaluation(evaluationPeriodId);
    }

    private void createEvaluationItems(Evaluation evaluation) {
        List<EvaluationItemDTO.Create> requests = new ArrayList<>();
        List<EvaluationItemDTO.CreateItemInfo> infoByAssessPostCodeForCreate =
                evaluationItemService.getInfoByAssessPostCodeForCreate(evaluation.getAssessPostCode().substring(0, evaluation.getAssessPostCode().indexOf("/")));
        infoByAssessPostCodeForCreate.forEach(info -> {
            List<EvaluationItemDTO.MeritTupleDTO> items = info.getMeritTuple();
            items.forEach(item -> {
                EvaluationItemDTO.Create evaluationItemDTO = new EvaluationItemDTO.Create();
                evaluationItemDTO.setEvaluationId(evaluation.getId());
                List<Long> instances = item.getInstances().stream().map(EvaluationItemDTO.InstanceTupleDTO::getId).toList();
                evaluationItemDTO.setInstanceIds(instances);
                evaluationItemDTO.setGroupTypeMeritId(item.getGroupTypeMeritId());
                evaluationItemDTO.setPostMeritComponentId(item.getPostMeritId());
                evaluationItemDTO.setInstanceGroupTypeMerits(item.getInstanceGroupTypeMerits());
                evaluationItemDTO.setPostMeritInstanceList(item.getPostMeritInstanceList());
                requests.add(evaluationItemDTO);
            });
        });
        evaluationItemService.createAll(requests);
    }

    private void updateEvaluationItems(Evaluation evaluation) {
        List<EvaluationItemDTO.Update> requests = new ArrayList<>();
        List<EvaluationItemDTO.Info> evaluationItemByEvalId = evaluationItemService.getByEvalId(evaluation.getId());
        evaluationItemByEvalId.forEach(info -> {
            EvaluationItemDTO.Update evaluationItemDTO = new EvaluationItemDTO.Update();
            evaluationItemDTO.setId(info.getId());
            evaluationItemDTO.setStatus("previous");
            evaluationItemDTO.setEvaluationId(info.getEvaluationId());
            evaluationItemDTO.setGroupTypeMeritId(info.getGroupTypeMeritId());
            evaluationItemDTO.setPostMeritComponentId(info.getPostMeritComponentId());
            requests.add(evaluationItemDTO);
        });
        evaluationItemService.updateAll(requests);
    }

}

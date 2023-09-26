package com.nicico.evaluation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.BatchDetailMapper;
import com.nicico.evaluation.model.BatchDetail;
import com.nicico.evaluation.repository.BatchDetailRepository;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.nicico.evaluation.utility.EvaluationConstant.*;

@RequiredArgsConstructor
@Service
public class BatchDetailService implements IBatchDetailService {

    private IBatchService batchService;
    private final BatchDetailMapper mapper;
    private final ObjectMapper objectMapper;
    private final PageableMapper pageableMapper;
    private final IKPITypeService kpiTypeService;
    private final ICatalogService catalogService;
    private final BatchDetailRepository repository;
    private final IInstanceService instanceService;
    private final ISpecialCaseService specialCaseService;
    private final IMeritComponentService meritComponentService;
    private final IPostMeritInstanceService postMeritInstanceService;
    private final IInstanceGroupTypeMeritService instanceGroupTypeMeritService;

    @Autowired
    public void setBatchService(@Lazy IBatchService batchService) {
        this.batchService = batchService;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH_DETAIL')")
    public BatchDetailDTO.Info get(Long id) {
        BatchDetail batchDetail = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(batchDetail);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH_DETAIL')")
    public BatchDetailDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<BatchDetail> batchDetails = repository.findAll(pageable);
        List<BatchDetailDTO.Info> batchDetailInfos = mapper.entityToDtoInfoList(batchDetails.getContent());

        BatchDetailDTO.Response response = new BatchDetailDTO.Response();
        BatchDetailDTO.SpecResponse specResponse = new BatchDetailDTO.SpecResponse();

        if (batchDetailInfos != null) {
            response.setData(batchDetailInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) batchDetails.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_BATCH_DETAIL')")
    public SearchDTO.SearchRs<BatchDetailDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @PreAuthorize("hasAuthority('C_BATCH_DETAIL')")
    public BaseResponse create(BatchDetailDTO.CreateList dto) {
        BaseResponse response = new BaseResponse();
        try {
            dto.getInputDetails().forEach(detail -> {
                BatchDetailDTO.Create create = new BatchDetailDTO.Create();
                try {
                    create.setInputDTO(objectMapper.writeValueAsString(detail));
                    create.setBatchId(dto.getBatchId());
                    create.setStatusCatalogId(catalogService.getByCode("Processing").getId());
                    BatchDetail batchDetail = mapper.dtoCreateToEntity(create);
                    repository.save(batchDetail);
                    response.setStatus(HttpStatus.OK.value());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            batchCreate(dto);
            return response;
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_BATCH_DETAIL')")
    public BatchDetailDTO.Info update(Long id, BatchDetailDTO.Update dto) {
        BatchDetail batchDetail = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(batchDetail, dto);
        try {
            BatchDetail save = repository.save(batchDetail);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('U_BATCH_DETAIL')")
    public void updateStatusAndExceptionTitleAndDescription(Long id, Long statusCatalogId, String exceptionTitle, String description) {
        Optional<BatchDetail> optionalBatchDetail = repository.findById(id);
        if (optionalBatchDetail.isPresent()) {
            BatchDetail batchDetail = optionalBatchDetail.get();
            batchDetail.setStatusCatalogId(statusCatalogId);
            batchDetail.setExceptionTitle(exceptionTitle);
            batchDetail.setDescription(description);
            repository.save(batchDetail);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_BATCH_DETAIL')")
    public void delete(Long id) {
        BatchDetail batchDetail = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(batchDetail);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_MERIT_COMPONENT')")
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست جزئیات اسناد گروهی");
        return new ExcelGenerator.ExcelDownload(body);
    }

    @Async("threadPoolAsync")
    void batchCreate(BatchDetailDTO.CreateList dto) {

        Long successCatalogId = catalogService.getByCode("Successful").getId();
        Long failCatalogId = catalogService.getByCode("Failed").getId();
        List<BatchDetail> batchDetailList = repository.findAllByBatchId(dto.getBatchId());
        switch (dto.getServiceType()) {
            case BATCH_CREATE_KPI_TYPE_EXCEL -> batchDetailList.forEach(detail -> {
                try {
                    KPITypeDTO.Create batchCreate = objectMapper.readValue(detail.getInputDTO(), KPITypeDTO.Create.class);
                    BaseResponse response = kpiTypeService.batchCreate(batchCreate);
                    String description = "نوع با عنوان " + batchCreate.getTitle();
                    if (response.getStatus() == 200)
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), successCatalogId, null, description);
                    else
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, response.getMessage(), description);
                } catch (JsonProcessingException e) {
                    updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, e.getMessage(), "");
                }
            });
            case BATCH_CREATE_POST_MERIT_INSTANCE_EXCEL -> batchDetailList.forEach(detail -> {
                try {
                    PostMeritInstanceDTO.BatchCreate batchCreate = objectMapper.readValue(detail.getInputDTO(), PostMeritInstanceDTO.BatchCreate.class);
                    BaseResponse response = postMeritInstanceService.batchCreate(batchCreate);
                    String description = "کدپست: " + batchCreate.getGroupPostCode() + " کدشایستگی: " + batchCreate.getMeritComponentCode() + " کدمصداق: " + batchCreate.getInstanceCode();
                    if (response.getStatus() == 200)
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), successCatalogId, null, description);
                    else
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, response.getMessage(), description);
                } catch (JsonProcessingException e) {
                    updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, e.getMessage(), "");
                }
            });
            case BATCH_CREATE_CHANGE_STATUS_SPECIAL_CASE -> batchDetailList.forEach(detail -> {
                try {
                    SpecialCaseDTO.ChangeStatusDTO batchCreate = objectMapper.readValue(detail.getInputDTO(), SpecialCaseDTO.ChangeStatusDTO.class);
                    BaseResponse response = specialCaseService.changeStatus(batchCreate);

                    String description = " کدپست : " + batchCreate.getAssessPostCode() + " نام ارزیاب شونده: " + batchCreate.getAssessFullName();
                    if (response.getStatus() == 200)
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), successCatalogId, null, description);
                    else
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, response.getMessage(), description);
                } catch (JsonProcessingException e) {
                    updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, e.getMessage(), "");
                }
            });
            case BATCH_CREATE_INSTANCE_EXCEL -> batchDetailList.forEach(detail -> {
                try {
                    InstanceDTO.Create batchCreate = objectMapper.readValue(detail.getInputDTO(), InstanceDTO.Create.class);
                    BaseResponse response = instanceService.batchCreate(batchCreate);

                    String description = "کد مصداق : " + batchCreate.getCode() + " عنوان مصداق: " + batchCreate.getTitle();
                    if (response.getStatus() == 200)
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), successCatalogId, null, description);
                    else
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, response.getMessage(), description);
                } catch (JsonProcessingException e) {
                    updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, e.getMessage(), "");
                }
            });
            case BATCH_CREATE_MERIT_COMPONENT_EXCEL -> batchDetailList.forEach(detail -> {
                try {
                    MeritComponentDTO.BatchCreate batchCreate = objectMapper.readValue(detail.getInputDTO(), MeritComponentDTO.BatchCreate.class);
                    BaseResponse response = meritComponentService.batchCreate(batchCreate);

                    String description = "کد مولفه شایستگی : " + batchCreate.getCode() + " عنوان مولفه شایستگی: " + batchCreate.getTitle();
                    if (response.getStatus() == 200)
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), successCatalogId, null, description);
                    else
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, response.getMessage(), description);
                } catch (JsonProcessingException e) {
                    updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, e.getMessage(), "");
                }
            });
            case BATCH_CREATE_INSTANCE_GROUP_TYPE_MERIT_EXCEL -> batchDetailList.forEach(detail -> {
                try {
                    InstanceGroupTypeMeritDTO.BatchCreate batchCreate = objectMapper.readValue(detail.getInputDTO(), InstanceGroupTypeMeritDTO.BatchCreate.class);
                    BaseResponse response = instanceGroupTypeMeritService.batchCreate(batchCreate);

                    String description = "کد مولفه شایستگی : " + batchCreate.getMeritComponentCode() + " کد گروه - نوع: "
                            + batchCreate.getGroupTypeCode() + " کد مصداق: " + batchCreate.getInstanceCode();
                    if (response.getStatus() == 200)
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), successCatalogId, null, description);
                    else
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, response.getMessage(), description);
                } catch (JsonProcessingException e) {
                    updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, e.getMessage(), "");
                }
            });
            case BATCH_CREATE_SPECIAL_CASE_EXCEL -> batchDetailList.forEach(detail -> {
                try {
                    SpecialCaseDTO.BatchCreate batchCreate = objectMapper.readValue(detail.getInputDTO(), SpecialCaseDTO.BatchCreate.class);
                    BaseResponse response = specialCaseService.batchCreate(batchCreate);

                    String description = "کد ملی ارزیاب کننده: " + batchCreate.getAssessorNationalCode() + " کد ملی ارزیاب شونده : " + batchCreate.getAssessNationalCode() ;
                    if (response.getStatus() == 200)
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), successCatalogId, null, description);
                    else
                        updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, response.getMessage(), description);
                } catch (JsonProcessingException e) {
                    updateStatusAndExceptionTitleAndDescription(detail.getId(), failCatalogId, e.getMessage(), "");
                }
            });
        }
        Long completedCatalogId = catalogService.getByCode("Completed").getId();
        batchService.updateStatus(dto.getBatchId(), completedCatalogId);
    }
}

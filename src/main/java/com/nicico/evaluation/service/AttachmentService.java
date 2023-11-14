package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IAttachmentService;
import com.nicico.evaluation.mapper.AttachmentMapper;
import com.nicico.evaluation.model.Attachment;
import com.nicico.evaluation.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttachmentService implements IAttachmentService {

    private final AttachmentMapper mapper;
    private final PageableMapper pageableMapper;
    private final AttachmentRepository repository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ATTACHMENT')")
    public AttachmentDTO.Info get(Long id) {
        Attachment Attachment = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(Attachment);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ATTACHMENT')")
    public List<AttachmentDTO.Info> getAllByObjectIdAndObjectTypeAndGroupId(Long objectId, String objectType, String groupId) {
        List<Attachment> attachments = repository.findAllByObjectIdAndObjectTypeAndGroupId(objectId, objectType, groupId);
        return mapper.entityToAttachInfoDtoList(attachments);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ATTACHMENT')")
    public AttachmentDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Attachment> Attachments = repository.findAll(pageable);
        List<AttachmentDTO.Info> AttachmentInfos = mapper.entityToDtoInfoList(Attachments.getContent());

        AttachmentDTO.Response response = new AttachmentDTO.Response();
        AttachmentDTO.SpecResponse specResponse = new AttachmentDTO.SpecResponse();

        if (AttachmentInfos != null) {
            response.setData(AttachmentInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) Attachments.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_ATTACHMENT')")
    public SearchDTO.SearchRs<AttachmentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);

    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<AttachmentDTO.BlobFileInfo> asyncExcelSearch(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoBlobFileInfo, request);
    }

    @Override
    @Transactional
    public AttachmentDTO.DownloadBlobInfo downloadAsyncExcel(Long id) {
        Optional<Attachment> optionalAttachment = repository.findById(id);
        Attachment attachment = optionalAttachment.orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        if (attachment.getStatus() != null && attachment.getStatus().equals(0)) {
            attachment.setStatus(1);
            repository.save(attachment);
        }
        return mapper.entityToDtoDownloadBlobInfo(attachment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_ATTACHMENT')")
    public AttachmentDTO.Info create(AttachmentDTO.Create dto) {
        Attachment Attachment = mapper.dtoCreateToEntity(dto);
        try {
            Attachment save = repository.save(Attachment);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    public void createBlobFile(AttachmentDTO.CreateBlobFile dto) {
        Attachment Attachment = mapper.dtoCreateBlobFileToEntity(dto);
        try {
            repository.save(Attachment);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_ATTACHMENT')")
    public AttachmentDTO.Info update(Long id, AttachmentDTO.Update dto) {
        Attachment Attachment = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(Attachment, dto);
        try {
            Attachment save = repository.save(Attachment);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_ATTACHMENT')")
    public void delete(Long id) {
        Attachment Attachment = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(Attachment);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

}

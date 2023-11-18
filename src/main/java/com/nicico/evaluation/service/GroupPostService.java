package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupPostDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IAttachmentService;
import com.nicico.evaluation.iservice.IGroupPostService;
import com.nicico.evaluation.mapper.GroupPostMapper;
import com.nicico.evaluation.model.GroupPost;
import com.nicico.evaluation.repository.GroupPostRepository;
import com.nicico.evaluation.utility.EvaluationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupPostService implements IGroupPostService {

    private final GroupPostMapper mapper;
    private final PageableMapper pageableMapper;
    private final GroupPostRepository repository;
    private final IAttachmentService attachmentService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_POST')")
    public GroupPostDTO.Info get(Long id) {
        GroupPost groupPost = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(groupPost);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_POST')")
    public GroupPostDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<GroupPost> groupPosts = repository.findAll(pageable);
        List<GroupPostDTO.Info> postInfos = mapper.entityToDtoInfoList(groupPosts.getContent());
        GroupPostDTO.Response response = new GroupPostDTO.Response();
        GroupPostDTO.SpecResponse specResponse = new GroupPostDTO.SpecResponse();

        if (postInfos != null) {
            response.setData(postInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) groupPosts.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_POST')")
    public SearchDTO.SearchRs<GroupPostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    public GroupPostDTO.Info getByCode(String code) {
        GroupPost groupPost = repository.findFirstByGroupPostCode(code).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "GroupPost", "گروه پستی یافت نشد"));
        return mapper.entityToDtoInfo(groupPost);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('R_GROUP_POST')")
    public String downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست پست");
        AttachmentDTO.CreateBlobFile createBlobFile = AttachmentDTO.CreateBlobFile.builder().blobFile(body).status(0).fileName(String.valueOf(new Date())).objectType(EvaluationConstant.GROUP_POST).build();
        attachmentService.createBlobFile(createBlobFile);
        return HttpStatus.OK.toString();
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void refreshViewGroupPost() {
        repository.refreshViewGroupPost();
    }

}

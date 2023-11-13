package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.AttachmentDTO;

import java.util.List;

public interface IAttachmentService {

    List<AttachmentDTO.Info> getAllByObjectIdAndObjectTypeAndGroupId(Long objectId, String objectType, String groupId);

    AttachmentDTO.SpecResponse list(int count, int startIndex);

    AttachmentDTO.Info get(Long id);

    AttachmentDTO.Info create(AttachmentDTO.Create dto);

    AttachmentDTO.InfoBlobFile createBlobFile(AttachmentDTO.CreateBlobFile dto);

    AttachmentDTO.Info update(Long id, AttachmentDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<AttachmentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}


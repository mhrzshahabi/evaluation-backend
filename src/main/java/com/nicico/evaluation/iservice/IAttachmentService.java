package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.AttachmentDTO;

public interface IAttachmentService {

    AttachmentDTO.SpecResponse list(int count, int startIndex);

    AttachmentDTO.Info get(Long id);

    AttachmentDTO.Info create(AttachmentDTO.Create dto);

    AttachmentDTO.Info update(AttachmentDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<AttachmentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}


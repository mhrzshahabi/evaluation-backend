package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    Attachment dtoCreateToEntity(AttachmentDTO.Create dto);

    AttachmentDTO.Info entityToDtoInfo(Attachment entity);

    List<AttachmentDTO.Info> entityToDtoInfoList(List<Attachment> entities);

    List<AttachmentDTO.Info> entityToAttachInfoDtoList(List<Attachment> entities);

    void update(@MappingTarget Attachment entity, AttachmentDTO.Update dto);
}

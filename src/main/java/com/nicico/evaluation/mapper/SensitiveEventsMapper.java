package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.model.SensitiveEvents;
import com.nicico.evaluation.service.AttachmentService;
import com.nicico.evaluation.utility.EvaluationConstant;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SensitiveEventsMapper {

    @Value("${ui.landing.fmsGroupId}")
    private String fmsGroupId;

    @Value("${ui.landing.fmsUrl}")
    private String fmsUrl;

    @Autowired
    AttachmentService attachmentService;

    public abstract SensitiveEvents dtoCreateToEntity(SensitiveEventsDTO.Create dto);

    @Mappings({
            @Mapping(target = "attachment", source = "id", qualifiedByName = "getAttachment"),
    })
    public abstract SensitiveEventsDTO.Info entityToDtoInfo(SensitiveEvents entity);

    public abstract List<SensitiveEventsDTO.Info> entityToDtoInfoList(List<SensitiveEvents> entities);

    public abstract void update(@MappingTarget SensitiveEvents entity, SensitiveEventsDTO.Update dto);

    @Named("getAttachment")
    AttachmentDTO.AttachInfo getAttachment(Long id) {
        AttachmentDTO.AttachInfo attachInfoDto = new AttachmentDTO.AttachInfo();
        List<AttachmentDTO.Info> allByObjectIdAndObjectTypeAndGroupId = attachmentService.getAllByObjectIdAndObjectTypeAndGroupId(id, EvaluationConstant.SENSITIVE_EVENTS, fmsGroupId);
        return attachInfoDto.setFmsUrl(fmsUrl).setInfoList(allByObjectIdAndObjectTypeAndGroupId);

    }
}

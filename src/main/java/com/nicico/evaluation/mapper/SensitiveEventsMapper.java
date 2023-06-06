package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.dto.SensitiveEventPersonDTO;
import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.model.SensitiveEventPerson;
import com.nicico.evaluation.model.SensitiveEvents;
import com.nicico.evaluation.service.AttachmentService;
import com.nicico.evaluation.utility.EvaluationConstant;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public abstract class SensitiveEventsMapper {

    @Value("${ui.landing.fmsGroupId}")
    private String fmsGroupId;

    @Value("${ui.landing.fmsUrl}")
    private String fmsUrl;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    SensitiveEventPersonMapper sensitiveEventPersonMapper;

    @Mappings({
            @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "toDate", source = "toDate", qualifiedByName = "convertDateToString")
    })
    public abstract SensitiveEvents dtoCreateToEntity(SensitiveEventsDTO.Create dto);

    @Mappings({
            @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "toDate", source = "toDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "attachment", source = "id", qualifiedByName = "getAttachmentInfo"),
            @Mapping(target = "sensitiveEventPersonList", source = "sensitiveEventPersonList", qualifiedByName = "getPersonDtoInfoList")
    })
    public abstract SensitiveEventsDTO.Info entityToDtoInfo(SensitiveEvents entity);

    public abstract List<SensitiveEventsDTO.Info> entityToDtoInfoList(List<SensitiveEvents> entities);

    @Mappings({
            @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "toDate", source = "toDate", qualifiedByName = "convertDateToString")
    })
    public abstract void update(@MappingTarget SensitiveEvents entity, SensitiveEventsDTO.Update dto);

    @Named("getAttachmentInfo")
    AttachmentDTO.AttachInfo getAttachmentInfo(Long id) {
        AttachmentDTO.AttachInfo attachInfoDto = new AttachmentDTO.AttachInfo();
        List<AttachmentDTO.Info> allByObjectIdAndObjectTypeAndGroupId = attachmentService.getAllByObjectIdAndObjectTypeAndGroupId(id, EvaluationConstant.SENSITIVE_EVENTS, fmsGroupId);
        return attachInfoDto.setFmsUrl(fmsUrl).setInfoList(allByObjectIdAndObjectTypeAndGroupId);
    }

    @Named("getPersonDtoInfoList")
    List<SensitiveEventPersonDTO.PersonInfo> getPersonDtoInfoList(List<SensitiveEventPerson> sensitiveEventPersonList) {
        return sensitiveEventPersonMapper.entityToDtoPersonInfoList(sensitiveEventPersonList);
    }
}

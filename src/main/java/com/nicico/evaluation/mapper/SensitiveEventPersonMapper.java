package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.dto.SensitiveEventPersonDTO;
import com.nicico.evaluation.iservice.IPersonService;
import com.nicico.evaluation.model.SensitiveEventPerson;
import com.nicico.evaluation.service.AttachmentService;
import com.nicico.evaluation.utility.EvaluationConstant;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SensitiveEventPersonMapper {

    @Autowired
    IPersonService personService;

    @Value("${ui.landing.fmsGroupId}")
    private String fmsGroupId;

    @Value("${ui.landing.fmsUrl}")
    private String fmsUrl;

    @Autowired
    AttachmentService attachmentService;

    public abstract SensitiveEventPerson dtoCreateToEntity(SensitiveEventPersonDTO.Create dto);

    @Mappings({
            @Mapping(target = "sensitiveEvent.eventDate", ignore = true),
            @Mapping(target = "sensitiveEvent.toDate", ignore = true),
            @Mapping(target = "firstName", source = "nationalCode", qualifiedByName = "getPersonFirstName"),
            @Mapping(target = "attachment", source = "id", qualifiedByName = "getAttachment"),
            @Mapping(target = "lastName", source = "nationalCode", qualifiedByName = "getPersonLastName")
    })
    public abstract SensitiveEventPersonDTO.Info entityToDtoInfo(SensitiveEventPerson entity);

    public abstract List<SensitiveEventPersonDTO.Info> entityToDtoInfoList(List<SensitiveEventPerson> entities);

    @Mappings({
            @Mapping(target = "firstName", source = "nationalCode", qualifiedByName = "getPersonFirstName"),
            @Mapping(target = "lastName", source = "nationalCode", qualifiedByName = "getPersonLastName")
    })
    public abstract SensitiveEventPersonDTO.PersonInfo entityToDtoPersonInfo(SensitiveEventPerson entity);

    public abstract List<SensitiveEventPersonDTO.PersonInfo> entityToDtoPersonInfoList(List<SensitiveEventPerson> entities);

    public abstract void update(@MappingTarget SensitiveEventPerson entity, SensitiveEventPersonDTO.Update dto);

    @Named("getPersonLastName")
    String getPersonLastName(String nationalCode) {
        return personService.getByNationalCode(nationalCode).getLastName();
    }

    @Named("getPersonFirstName")
    String getPersonFirstName(String nationalCode) {
        return personService.getByNationalCode(nationalCode).getFirstName();
    }


    @Named("getAttachment")
    AttachmentDTO.AttachInfo getAttachment(Long id) {
        AttachmentDTO.AttachInfo attachInfoDto = new AttachmentDTO.AttachInfo();
        List<AttachmentDTO.Info> allByObjectIdAndObjectTypeAndGroupId = attachmentService.getAllByObjectIdAndObjectTypeAndGroupId(id, EvaluationConstant.SENSITIVE_EVENT_PERSON, fmsGroupId);
        return attachInfoDto.setFmsUrl(fmsUrl).setInfoList(allByObjectIdAndObjectTypeAndGroupId);
    }
}

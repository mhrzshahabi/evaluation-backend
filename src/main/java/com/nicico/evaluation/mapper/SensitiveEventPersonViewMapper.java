package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.model.SensitiveEventPersonView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensitiveEventPersonViewMapper {

    SensitiveEventsDTO.SensitiveEventPersonInfo entityToDtoInfo(SensitiveEventPersonView entity);

    List<SensitiveEventsDTO.SensitiveEventPersonInfo> entityToDtoInfoList(List<SensitiveEventPersonView> entities);

}

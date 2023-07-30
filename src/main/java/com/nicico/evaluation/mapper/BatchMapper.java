package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.dto.WebSocketDTO;
import com.nicico.evaluation.model.Batch;
import org.mapstruct.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BatchMapper {

    Batch dtoCreateToEntity(BatchDTO.Create dto);

    @Mappings({
            @Mapping(target = "titleCatalog", source = "titleCatalog.title"),
            @Mapping(target = "statusCatalog", source = "statusCatalog.title")
    })
    BatchDTO.Info entityToDtoInfo(Batch entity);

    List<BatchDTO.Info> entityToDtoInfoList(List<Batch> entities);

    void update(@MappingTarget Batch entity, BatchDTO.Update dto);

    AttachmentDTO.Create dtoCreateToAttachmentDtoCreate(BatchDTO.Create dto);


    @Mappings({
            @Mapping(target = "title", source = "titleCatalog.title"),
            @Mapping(target = "progressPercent", source = "entity", qualifiedByName = "getProgressPercent"),
            @Mapping(target = "lastModifiedDate", source = "lastModifiedDate", qualifiedByName = "changeToLocalTime")
    })
    WebSocketDTO entityToWebSocketDto(Batch entity);

    List<WebSocketDTO> entityToWebSocketDtoList(List<Batch> entities);

    @Named("getProgressPercent")
    default Integer getProgressPercent(Batch entity) {
        if ((entity.getSuccessfulNumber() + entity.getFailedNumber()) == 0)
            return 0;
        else
            return ((entity.getSuccessfulNumber() + entity.getFailedNumber()) / entity.getTotal()) * 100;
    }

    @Named("changeToLocalTime")
    default Date changeToLocalTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 3);
        calendar.add(Calendar.MINUTE, 30);
        return calendar.getTime();
    }
}

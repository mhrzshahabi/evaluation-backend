package com.nicico.evaluation.mapper;

import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.model.Attachment;
import org.mapstruct.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    Attachment dtoCreateToEntity(AttachmentDTO.Create dto);

    Attachment dtoCreateBlobFileToEntity(AttachmentDTO.CreateBlobFile dto);

    AttachmentDTO.Info entityToDtoInfo(Attachment entity);

    @Mappings({
            @Mapping(target = "createdDate", source = "createdDate"),
            @Mapping(target = "createdTime", source = "createdDate", qualifiedByName = "convertTimeToString")
    })
    AttachmentDTO.BlobFileInfo entityToDtoBlobFileInfo(Attachment entity);

    AttachmentDTO.DownloadBlobInfo entityToDtoDownloadBlobInfo(Attachment entity);

    List<AttachmentDTO.Info> entityToDtoInfoList(List<Attachment> entities);

    List<AttachmentDTO.Info> entityToAttachInfoDtoList(List<Attachment> entities);

    void update(@MappingTarget Attachment entity, AttachmentDTO.Update dto);

    @Named("convertDateToString")
    default String convertDateToString(Date date) {
        if (Objects.isNull(date)) return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        return DateUtil.convertMiToKh(dateFormat.format(date));
    }

    @Named("convertTimeToString")
    default String convertTimeToString(Date date) {
        if (Objects.isNull(date)) return null;
        DateFormat dateFormat = new SimpleDateFormat("H:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        return dateFormat.format(date);
    }

}

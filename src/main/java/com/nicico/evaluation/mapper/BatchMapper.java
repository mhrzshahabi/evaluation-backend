package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.iservice.IBatchDetailService;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.model.Batch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BatchMapper {

    @Lazy
    @Autowired
    private ICatalogService catalogService;

    @Lazy
    @Autowired
    private IBatchDetailService batchDetailService;

    public abstract Batch dtoCreateToEntity(BatchDTO.Create dto);

    @Mappings({
            @Mapping(target = "titleCatalog", source = "titleCatalogId", qualifiedByName = "getTitleCatalogFromTitleCatalogId"),
            @Mapping(target = "statusCatalog", source = "statusCatalogId", qualifiedByName = "getStatusCatalogFromStatusCatalogId"),
            @Mapping(target = "successfulNumber", source = "id", qualifiedByName = "getSuccessfulNumberByBatchId"),
            @Mapping(target = "failedNumber", source = "id", qualifiedByName = "getFailedNumberByBatchId"),
            @Mapping(target = "total", source = "id", qualifiedByName = "getTotalNumberByBatchId"),
            @Mapping(target = "progress", source = "id", qualifiedByName = "getProgressByBatchId")
    })
    public abstract BatchDTO.Info entityToDtoInfo(Batch entity);

//    List<BatchDTO.Info> entityToDtoInfoList(List<Batch> entities);
//
//    void update(@MappingTarget Batch entity, BatchDTO.Update dto);

    @Named("getTitleCatalogFromTitleCatalogId")
    String getTitleCatalogFromTitleCatalogId(Long titleCatalogId) {
        return catalogService.get(titleCatalogId).getTitle();
    }

    @Named("getStatusCatalogFromStatusCatalogId")
    String getStatusCatalogFromStatusCatalogId(Long statusCatalogId) {
        return catalogService.get(statusCatalogId).getTitle();
    }

    @Named("getSuccessfulNumberByBatchId")
    Integer getSuccessfulNumberByBatchId(Long batchId) {
        List<BatchDetailDTO.Info> batchDetailList = batchDetailService.getBatchDetailListByBatchId(batchId);
        return (int) batchDetailList.stream().filter(item -> item.getStatusCatalog().equals("موفق")).count();
    }

    @Named("getFailedNumberByBatchId")
    Integer getFailedNumberByBatchId(Long batchId) {
        List<BatchDetailDTO.Info> batchDetailList = batchDetailService.getBatchDetailListByBatchId(batchId);
        return (int) batchDetailList.stream().filter(item -> item.getStatusCatalog().equals("ناموفق")).count();
    }

    @Named("getTotalNumberByBatchId")
    Integer getTotalNumberByBatchId(Long batchId) {
        return batchDetailService.getBatchDetailListByBatchId(batchId).size();
    }

    @Named("getProgressByBatchId")
    Integer getProgressByBatchId(Long batchId) {
        List<BatchDetailDTO.Info> batchDetailList = batchDetailService.getBatchDetailListByBatchId(batchId);
        int successful = Math.toIntExact(batchDetailList.stream().filter(item -> item.getStatusCatalog().equals("موفق")).count());
        if (successful != 0)
            return successful/batchDetailList.size();
        else
            return 0;
    }

}

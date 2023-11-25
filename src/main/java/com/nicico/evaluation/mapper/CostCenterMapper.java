package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.CostCenterDTO;
import com.nicico.evaluation.model.CostCenter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CostCenterMapper {

    CostCenterDTO.Info entityToDtoInfo(CostCenter costCenter);
    List<CostCenterDTO.Info> entityToDtoInfoList(List<CostCenter> costCenters);
}

package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.iservice.IPersonService;
import com.nicico.evaluation.model.Evaluation;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public abstract class EvaluationMapper {

    @Lazy
    @Autowired
    IPersonService personService;

    public abstract EvaluationDTO.Excel entityToDtoExcel(Evaluation entity);

    public abstract List<EvaluationDTO.Excel> entityToDtoExcelList(List<Evaluation> entities);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertDateToString")
    })
    public abstract Evaluation dtoCreateToEntity(EvaluationDTO.Create dto);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertDateToString")
    })
    public abstract List<Evaluation> dtoCreateToEntityList(List<EvaluationDTO.Create> dto);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertStringToDate"),
    })
    public abstract EvaluationDTO.Update entityToUpdateDto(Evaluation entity);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertStringToDate"),
    })
    public abstract EvaluationDTO.Info entityToDtoInfo(Evaluation entity);

    public abstract List<EvaluationDTO.Info> entityToDtoInfoList(List<Evaluation> entities);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertDateToString")
    })
    public abstract void update(@MappingTarget Evaluation entity, EvaluationDTO.Update dto);


}

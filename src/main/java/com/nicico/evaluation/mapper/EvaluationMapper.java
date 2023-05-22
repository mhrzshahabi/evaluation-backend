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


    public abstract Evaluation dtoCreateToEntity(EvaluationDTO.Create dto);


    public abstract List<Evaluation> dtoCreateToEntityList(List<EvaluationDTO.Create> dto);


    public abstract EvaluationDTO.Update entityToUpdateDto(Evaluation entity);


    public abstract EvaluationDTO.Info entityToDtoInfo(Evaluation entity);

    public abstract List<EvaluationDTO.Info> entityToDtoInfoList(List<Evaluation> entities);


    public abstract void update(@MappingTarget Evaluation entity, EvaluationDTO.Update dto);


}

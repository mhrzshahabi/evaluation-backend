package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.model.Evaluation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationMapper {

    EvaluationDTO.Excel entityToDtoExcel(Evaluation entity);

    List<EvaluationDTO.Excel> entityToDtoExcelList(List<Evaluation> entities);

    Evaluation dtoCreateToEntity(EvaluationDTO.Create dto);

    EvaluationDTO.Info entityToDtoInfo(Evaluation entity);

    List<EvaluationDTO.Info> entityToDtoInfoList(List<Evaluation> entities);

    void update(@MappingTarget Evaluation entity, EvaluationDTO.Update dto);

}

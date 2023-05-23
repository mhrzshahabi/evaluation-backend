package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.model.Evaluation;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public interface EvaluationMapper {

    EvaluationDTO.Excel entityToDtoExcel(Evaluation entity);

    List<EvaluationDTO.Excel> entityToDtoExcelList(List<Evaluation> entities);

    Evaluation dtoCreateToEntity(EvaluationDTO.Create dto);

    List<Evaluation> dtoCreateToEntityList(List<EvaluationDTO.Create> dto);

    EvaluationDTO.Update entityToUpdateDto(Evaluation entity);

    EvaluationDTO.Info entityToDtoInfo(Evaluation entity);

    List<EvaluationDTO.Info> entityToDtoInfoList(List<Evaluation> entities);

    void update(@MappingTarget Evaluation entity, EvaluationDTO.Update dto);


}

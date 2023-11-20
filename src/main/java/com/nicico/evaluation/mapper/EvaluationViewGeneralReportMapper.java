package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationGeneralReportDTO;
import com.nicico.evaluation.model.EvaluationGeneralReportView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationViewGeneralReportMapper {

    EvaluationGeneralReportDTO.Info entityToDtoInfo(EvaluationGeneralReportView evaluationView);

    List<EvaluationGeneralReportDTO.Info> entityToDtoInfoList(List<EvaluationGeneralReportView> evaluationViews);

    EvaluationGeneralReportDTO.Excel entityToDtoExcel(EvaluationGeneralReportView evaluationView);

    EvaluationGeneralReportDTO.Excel infoDtoToDtoExcel(EvaluationGeneralReportDTO.Info evaluationView);

    List<EvaluationGeneralReportDTO.Excel> infoDtoToDtoExcelList(List<EvaluationGeneralReportDTO.Info> evaluationViewList);

}

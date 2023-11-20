package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationCostCenterReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationCostCenterReportViewRepository extends JpaRepository<EvaluationCostCenterReportView, Long>, JpaSpecificationExecutor<EvaluationCostCenterReportView> {

}

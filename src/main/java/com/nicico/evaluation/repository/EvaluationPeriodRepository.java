package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationPeriodRepository extends JpaRepository<EvaluationPeriod, Long>, JpaSpecificationExecutor<EvaluationPeriod> {

    List<EvaluationPeriod> findAllByStatusCatalogId(Long statusCatalogId);

//    List<EvaluationPeriod> findAllByStartDateAssessment();
}

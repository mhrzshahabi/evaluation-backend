package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationPeriodRepository extends JpaRepository<EvaluationPeriod, Long>, JpaSpecificationExecutor<EvaluationPeriod> {
}

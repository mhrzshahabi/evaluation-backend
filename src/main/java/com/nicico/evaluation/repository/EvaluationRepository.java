package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, JpaSpecificationExecutor<Evaluation> {
    List<Evaluation> findByEvaluationPeriodIdAndAssessPostCode(Long id, String assessPostCode);
    List<Evaluation> findAllByIdIn(List<Long> id);
}

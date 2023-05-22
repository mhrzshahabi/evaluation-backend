package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationItemInstanceRepository extends JpaRepository<EvaluationItemInstance, Long>, JpaSpecificationExecutor<EvaluationItemInstance> {
    List<EvaluationItemInstance> getAllByEvaluationItemId(Long evaluationItemId);
}

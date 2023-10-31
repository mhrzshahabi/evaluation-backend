package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationViewRepository extends JpaRepository<EvaluationView, Long>, JpaSpecificationExecutor<EvaluationView> {

}

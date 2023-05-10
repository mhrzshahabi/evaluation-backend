package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationItemRepository extends JpaRepository<EvaluationItem, Long>, JpaSpecificationExecutor<EvaluationItem> {
}

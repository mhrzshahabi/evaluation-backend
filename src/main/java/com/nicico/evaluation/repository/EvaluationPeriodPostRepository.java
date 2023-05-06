package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationPeriodPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationPeriodPostRepository extends JpaRepository<EvaluationPeriodPost, Long>, JpaSpecificationExecutor<EvaluationPeriodPost> {

    List<EvaluationPeriodPost> findAllByPostCode(String postCode);

    List<EvaluationPeriodPost> findAllByEvaluationPeriodId(Long id);

}

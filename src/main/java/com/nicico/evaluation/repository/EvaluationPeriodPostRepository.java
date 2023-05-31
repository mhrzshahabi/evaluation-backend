package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationPeriodPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EvaluationPeriodPostRepository extends JpaRepository<EvaluationPeriodPost, Long>, JpaSpecificationExecutor<EvaluationPeriodPost> {

    List<EvaluationPeriodPost> findAllByEvaluationPeriodId(Long id);

    List<EvaluationPeriodPost> findAllByPostCodeIn(Set<String> postCode);

    Optional<EvaluationPeriodPost> findByEvaluationPeriodIdAndPostCode(Long evaluationPeriodPost, String postCode);

    void deleteByEvaluationPeriodId(Long evaluationPeriodPost);

    void deleteByEvaluationPeriodIdAndPostCode(Long evaluationPeriodPost, String postCode);
}

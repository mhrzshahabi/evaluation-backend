package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.PostMeritInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMeritInstanceRepository extends JpaRepository<PostMeritInstance, Long>, JpaSpecificationExecutor<PostMeritInstance> {

    List<PostMeritInstance> findAllByPostMeritComponentId(Long postMeritId);
}

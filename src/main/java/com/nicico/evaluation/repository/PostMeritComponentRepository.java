package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.PostMeritComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMeritComponentRepository extends JpaRepository<PostMeritComponent, Long>, JpaSpecificationExecutor<PostMeritComponent> {
}

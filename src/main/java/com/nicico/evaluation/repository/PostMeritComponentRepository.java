package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.PostMeritComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMeritComponentRepository extends JpaRepository<PostMeritComponent, Long>, JpaSpecificationExecutor<PostMeritComponent> {

    List<PostMeritComponent> findAllByGroupPostCode(String groupPostCode);
}

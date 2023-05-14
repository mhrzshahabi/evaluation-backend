package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.PostRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRelationRepository extends JpaRepository<PostRelation, Long>, JpaSpecificationExecutor<PostRelation> {

}

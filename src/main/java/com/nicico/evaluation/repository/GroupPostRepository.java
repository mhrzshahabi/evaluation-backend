package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Long>, JpaSpecificationExecutor<GroupPost> {
}

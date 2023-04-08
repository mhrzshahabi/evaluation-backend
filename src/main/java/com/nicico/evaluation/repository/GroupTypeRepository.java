package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.GroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTypeRepository extends JpaRepository<GroupType, Long>, JpaSpecificationExecutor<GroupType> {}

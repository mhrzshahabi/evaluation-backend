package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.GroupTypeMerit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTypeMeritRepository extends JpaRepository<GroupTypeMerit, Long>, JpaSpecificationExecutor<GroupTypeMerit> {}

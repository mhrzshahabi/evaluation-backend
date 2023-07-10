package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.GroupTypeMerit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupTypeMeritRepository extends JpaRepository<GroupTypeMerit, Long>, JpaSpecificationExecutor<GroupTypeMerit> {

    List<GroupTypeMerit> getAllByGroupTypeId(@Param("groupTypeId") Long groupTypeId);
}

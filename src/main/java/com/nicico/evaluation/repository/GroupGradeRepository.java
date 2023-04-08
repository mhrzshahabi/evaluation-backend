package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.GroupGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupGradeRepository extends JpaRepository<GroupGrade, Long>, JpaSpecificationExecutor<GroupGrade> {
}

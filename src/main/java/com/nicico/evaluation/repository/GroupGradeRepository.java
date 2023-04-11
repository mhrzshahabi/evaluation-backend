package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.GroupGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupGradeRepository extends JpaRepository<GroupGrade, Long>, JpaSpecificationExecutor<GroupGrade> {
    List<GroupGrade> findAllByGradeIdIn(List<Long> gradeIds);
    List<GroupGrade> findAllByGroupId(Long gradeId);
    Optional<GroupGrade> findFirstByGradeCodeAndGradeId(String gradeCode,Long GradeId);
}

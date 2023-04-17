package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>, JpaSpecificationExecutor<Grade> {
    List<Grade> getAllByCodeIn(List<String> codes);
    Grade get(List<String> codes);
}

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
    @Query(" from GroupTypeMerit groupTypeMerit " +
            " join MeritComponent merit on merit.id = groupTypeMerit.meritComponentId " +
            " join InstanceGroupTypeMerit instanceGroupTypeMerit on instanceGroupTypeMerit.groupTypeMeritId = groupTypeMerit.id" +
            " join Instance instance on instance.id = instanceGroupTypeMerit.instanceId" +
            " join GroupType groupType on groupType.id = groupTypeMerit.groupTypeId " +
            " join KPIType type on type.id = groupType.kpiTypeId " +
            " join Group gr on gr.id = groupType.groupId " +
            " join GroupGrade groupGrade on gr.id =  groupGrade.groupId " +
            " join Grade grade on groupGrade.gradeId= grade.id  " +
            " join GroupPost post on post.postGradeCode= grade.code  " +
            " where post.groupPostCode = :assessPostCode")
    List<GroupTypeMerit> getTypeMeritInstanceByAssessPostCode(@Param("assessPostCode") String assessPostCode);

    List<GroupTypeMerit> getAllByGroupTypeId(@Param("groupTypeId") Long groupTypeId);
}

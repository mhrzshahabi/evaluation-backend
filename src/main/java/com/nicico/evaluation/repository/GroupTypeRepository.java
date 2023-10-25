package com.nicico.evaluation.repository;

import com.nicico.evaluation.dto.GroupTypeByGroupByDTO;
import com.nicico.evaluation.model.GroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupTypeRepository extends JpaRepository<GroupType, Long>, JpaSpecificationExecutor<GroupType> {
    @Query(" from GroupType groupType  " +
            " join KPIType type on type.id = groupType.kpiTypeId " +
            " join Catalog catalog on catalog.id = type.levelDefCatalogId " +
            " join Group gr on gr.id = groupType.groupId " +
            " join GroupGrade groupGrade on gr.id =  groupGrade.groupId " +
            " join Grade grade on groupGrade.gradeId= grade.id  " +
            " join GroupPost post on post.postGradeCode= grade.code  " +
            " where post.groupPostCode = :assessPostCode and catalog.code = :levelDef ")
    List<GroupType> getTypeByAssessPostCode(@Param("assessPostCode") String assessPostCode, @Param("levelDef") String levelDef);

    List<GroupType> getAllByGroupId(@Param("groupId") Long groupId);

    GroupType getByCode(@Param("code") String code);

    GroupType getByGroupIdAndKpiTypeId(@Param("groupId") Long groupId, @Param("kpiTypeId") Long kpiTypeId);

    @Query(value = """
            SELECT
                distinct grouptype.*
            FROM
                tbl_group_type    grouptype
                JOIN tbl_group_grade   groupgrade ON groupgrade.group_id = grouptype.group_id
                JOIN tbl_group group1 on group1.id = groupGrade.group_id
                JOIN tbl_kpi_type kpiType on kpiType.id = groupType.kpi_type_id
                JOIN view_post   post ON post.post_grade_id = groupgrade.grade_id
                where post.post_code in (
                select c_post_code from tbl_evaluation_period_post periodPost where periodPost.evaluation_period_id = :periodId
                )
            """, nativeQuery = true)
    List<GroupType> findAllByPeriodId(Long periodId);

    @Query(value = """
            SELECT
                distinct grouptype.*
            FROM
                tbl_group_type    grouptype
                JOIN tbl_group_grade   groupgrade ON groupgrade.group_id = grouptype.group_id
                JOIN tbl_group group1 on group1.id = groupGrade.group_id
                JOIN tbl_kpi_type kpiType on kpiType.id = groupType.kpi_type_id
                JOIN view_post   post ON post.post_grade_id = groupgrade.grade_id
                where post.post_code in (
                :postCodes
                )
            """, nativeQuery = true)
    List<GroupType> findAllByPostCodes(List<String> postCodes);

    @Query(value = """
            SELECT
               MAX(group1.c_title) title,
               gtype.group_id groupId,
               SUM(gtype.n_weight) totalWeight,
               CASE COUNT(ktype.id) 
                   WHEN 3 THEN
                       1
                   ELSE
                       0
               END hasAllKpiType
            FROM
                tbl_group_type gtype
                join tbl_kpi_type ktype on ktype.id = gtype.KPI_TYPE_ID
                join tbl_group group1 on group1.id = gtype.group_id
            group by gtype.group_id
            OFFSET :pageNumber ROWS FETCH NEXT :pageSize ROWS ONLY
            """, nativeQuery = true)
    List<GroupTypeByGroupByDTO.Resp> findAllByGroupBy(int pageNumber, int pageSize);

    @Query(value = """
            SELECT
                  COUNT(*)
            FROM
              (
                  SELECT
                      MAX(group1.c_title) title,
                      gtype.group_id groupid,
                      SUM(gtype.n_weight) totalweight,
                      CASE COUNT(ktype.id)\s
                             WHEN 3 THEN
                                 1
                             ELSE
                                 0
                         END hasAllKpiType
                  FROM
                      tbl_group_type   gtype
                      JOIN tbl_kpi_type     ktype ON ktype.id = gtype.kpi_type_id
                      JOIN tbl_group        group1 ON group1.id = gtype.group_id
                  GROUP BY
                      gtype.group_id
              )
            """, nativeQuery = true)
    Long totalCountByGroupBy();

}

package com.nicico.evaluation.repository;

import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.model.EvaluationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationPeriodRepository extends JpaRepository<EvaluationPeriod, Long>, JpaSpecificationExecutor<EvaluationPeriod> {

    List<EvaluationPeriod> findAllByStatusCatalogId(Long statusCatalogId);

    @Query(value = """
            SELECT DISTINCT
                evalperiod.*
            FROM
                tbl_evaluation          eval
                JOIN tbl_evaluation_period   evalperiod ON evalperiod.id = eval.evaluation_period_id
            WHERE
                eval.c_created_by = :creator
                AND evalperiod.C_VALIDATION_START_DATE = :toDayDate
                AND eval.status_catalog_id = :statusId
            """, nativeQuery = true)
    List<EvaluationPeriod> findAllByCreatorAndStartDateValidation(String creator, String toDayDate, Long statusId);

    @Query(value = """
            SELECT 
                evalperiod.C_TITLE periodTitle ,
                '[' || to_char(to_date( evalperiod.C_VALIDATION_END_DATE, 'YYYY-MM-DD') - to_date( '1402/06/28','YYYY-MM-DD') + 2) || ']' as remainDate
            FROM
                tbl_evaluation          eval
                JOIN tbl_evaluation_period   evalperiod ON evalperiod.id = eval.evaluation_period_id
            WHERE
                eval.c_created_by = :creator
                AND evalperiod.C_VALIDATION_START_DATE < :toDayDate
                AND evalperiod.C_VALIDATION_END_DATE >= :toDayDate
                AND eval.status_catalog_id = :statusId
            """, nativeQuery = true)
    List<EvaluationPeriodDTO.RemainDate> findAllByCreatorAndRemainDateToEndDateValidation(String creator, String toDayDate, Long statusId);

    @Query(value = """
            select * 
            from tbl_evaluation eval
            join tbl_evaluation_period evalPeriod on evalPeriod.id = eval.EVALUATION_PERIOD_ID
            where
                eval.C_ASSESSOR_NATIONAL_CODE = :nationalCode
            and evalPeriod.C_START_DATE_ASSESSMENT = :toDayDate
            """, nativeQuery = true)
    List<EvaluationPeriod> findAllByAssessorAndStartDateAssessment(String nationalCode, String toDayDate);

    @Query(value = """
            select
                 evalperiod.C_TITLE periodTitle ,
                 '[' || to_char( to_date( evalperiod.C_END_DATE_ASSESSMENT, 'YYYY-MM-DD') - to_date( :toDayDate,'YYYY-MM-DD') + 2) || ']' as remainDate
            from tbl_evaluation eval
            join tbl_evaluation_period evalPeriod on evalPeriod.id = eval.EVALUATION_PERIOD_ID
            where
                eval.C_ASSESSOR_NATIONAL_CODE = :nationalCode
                and evalPeriod.C_START_DATE_ASSESSMENT < :toDayDate
                AND evalperiod.C_END_DATE_ASSESSMENT >= :toDayDate
                AND eval.status_catalog_id = :statusId
            """, nativeQuery = true)
    List<EvaluationPeriodDTO.RemainDate> findAllByAssessorAndStartDateAssessmentAndStatusId(String nationalCode, String toDayDate, Long statusId);

    @Modifying
    @Query(value = "update EvaluationPeriod evalPeriod set evalPeriod.statusCatalogId = :statusCatalogId where evalPeriod.endDateAssessment < :toDayDate")
    void updateEvaluationPeriodStatus(Long statusCatalogId, String toDayDate);

    @Query(value = "select period.* from tbl_evaluation_period period " +
            " join tbl_evaluation eval on eval.EVALUATION_PERIOD_ID = period.id " +
            " where eval.STATUS_CATALOG_ID = :statusCatalogId and eval.C_ASSESS_NATIONAL_CODE = :assessNationalCode and " +
            " period.STATUS_CATALOG_ID = :periodStatusId " +
            " OFFSET :pageNumber ROWS FETCH NEXT :pageSize ROWS ONLY ", nativeQuery = true)
    List<EvaluationPeriod> findAllByAssessNationalCodeAndStatusCatalogId(String assessNationalCode, Long statusCatalogId, Long periodStatusId, int pageNumber, int pageSize);
}

package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long>, JpaSpecificationExecutor<Batch> {

    @Query(value = """
            SELECT
                *
            FROM
                (
                    SELECT
                        *
                    FROM
                        tbl_batch
                    WHERE
                        tbl_batch.status_catalog_id =:statusCatalogInProgressId
                )
            UNION
            SELECT
                *
            FROM
                (
                    SELECT
                        *
                    FROM
                        tbl_batch
                    WHERE
                        tbl_batch.status_catalog_id =:statusCatalogCompleteId
                        AND ROWNUM <= 3
                    ORDER BY
                        tbl_batch.d_last_modified_date DESC
                )
                          """, nativeQuery = true)
    List<Batch> getNeededDataForWebSocket(Long statusCatalogInProgressId, Long statusCatalogCompleteId);
}

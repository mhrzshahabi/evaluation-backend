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
                        tbl_batch.status_catalog_id = :statusCatalogInProgressId
                )
            UNION
            SELECT
                *
            FROM
                (
                    SELECT
                        id,
                        title_catalog_id,
                        status_catalog_id,
                        c_created_by,
                        d_created_date,
                        c_last_modified_by,
                        d_last_modified_date,
                        RANK()
                        OVER(
                            ORDER BY
                                d_last_modified_date DESC
                        ) AS modified_date_rank
                    FROM
                        tbl_batch
                    WHERE
                        tbl_batch.status_catalog_id = :statusCatalogCompleteId
                ) completed
            WHERE
                completed.modified_date_rank <= 3
                          """, nativeQuery = true)
    List<Batch> getNeededDataForWebSocket(Long statusCatalogInProgressId, Long statusCatalogCompleteId);
}

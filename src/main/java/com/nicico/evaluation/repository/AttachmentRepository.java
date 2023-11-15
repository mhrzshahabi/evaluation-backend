package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, JpaSpecificationExecutor<Attachment> {

    List<Attachment> findAllByObjectIdAndObjectTypeAndGroupId(Long objectId, String objectType, String groupId);

    @Modifying
    @Query(value = """
                DELETE FROM tbl_attachment
                WHERE
                    n_status IS NOT NULL
            """, nativeQuery = true)
    void removeExcelExports();
}

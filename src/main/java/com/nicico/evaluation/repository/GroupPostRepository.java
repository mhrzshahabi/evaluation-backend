package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Long>, JpaSpecificationExecutor<GroupPost> {

    Optional<GroupPost> findFirstByGroupPostCode(String code);

    @Modifying
    @Query(value = """
            BEGIN
                dbms_mview.refresh('view_group_post');
            END;
            """, nativeQuery = true)
    void refreshViewGroupPost();
}

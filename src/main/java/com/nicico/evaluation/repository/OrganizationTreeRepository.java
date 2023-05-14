package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.OrganizationTree;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationTreeRepository extends JpaRepository<OrganizationTree, Long>, JpaSpecificationExecutor<OrganizationTree> {
    List<OrganizationTree> findAllByOrgStructureId(Long orgStructureId);

    List<OrganizationTree> findAllByPostParentId(Long postParentId, Pageable pageable);

    Long countByPostParentId(Long postId);
}

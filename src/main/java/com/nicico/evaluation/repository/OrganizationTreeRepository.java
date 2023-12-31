package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.OrganizationTree;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationTreeRepository extends JpaRepository<OrganizationTree, Long>, JpaSpecificationExecutor<OrganizationTree> {

    List<OrganizationTree> findAllByOrgStructureId(Long orgStructureId);

    List<OrganizationTree> findAllByPostParentIdAndOrgStructureId(Long postParentId, Long orgStructureId, Pageable pageable);

    Optional<OrganizationTree> findFirstByPostCode(String postCode);

    Optional<OrganizationTree> findFirstByNationalCode(String nationalCode);

    Long countByPostParentId(Long postId);

    List<OrganizationTree> findAllByPostIdInAndNationalCodeIsNotNullOrderByPostLevelDesc(List<Long> postIds);

    @Query(value = "select postCode from OrganizationTree where nationalCodeParent= :nationalCode and nationalCode is not Null")
    List<String> getByParentNationalCode(String nationalCode);

}

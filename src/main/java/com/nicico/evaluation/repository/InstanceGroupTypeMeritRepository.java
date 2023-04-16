package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstanceGroupTypeMeritRepository extends JpaRepository<InstanceGroupTypeMerit, Long>, JpaSpecificationExecutor<InstanceGroupTypeMerit> {
    List<InstanceGroupTypeMerit> getAllByGroupTypeMeritId(Long id);
}

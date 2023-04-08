package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.model.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceRepository extends JpaRepository<Instance, Long>, JpaSpecificationExecutor<Instance> {

}

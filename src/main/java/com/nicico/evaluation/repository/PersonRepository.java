package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    Person findAllByNationalCode(String nationalCode);
    Person findByPostCode(String postCode);

}

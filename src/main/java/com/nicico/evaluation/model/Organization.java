package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Subselect("select * from view_organization")
@DiscriminatorValue("viewOrganization")
public class Organization {
    @Id
    @Column(name = "company_id")
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "org_structure_id")
    private Long orgStructureId;

    @Column(name = "org_structure_name")
    private String orgStructureName;

    @Column(name = "org_str_start_date")
    private String startDate;

    @Column(name = "org_str_end_date")
    private String endDate;

}

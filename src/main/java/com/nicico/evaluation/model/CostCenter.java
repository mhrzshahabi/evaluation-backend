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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Subselect("select * from view_cost_center")
@DiscriminatorValue("viewCostCenter")
public class CostCenter {

    @Id
    private Long id;

    @Column(name = "COST_CENTER_CODE")
    private String costCenterCode;

    @Column(name = "COST_CENTER_TITLE")
    private String costCenterTitle;

}
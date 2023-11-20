package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Subselect("select  * from view_evaluation_cost_center_report")
@DiscriminatorValue("viewEvaluationCostCenterReport")
public class EvaluationCostCenterReportView {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "evaluation_period_id")
    private Long evaluationPeriodId;

    @Column(name = "cost_center_code")
    private String costCenterCode;

    @Column(name = "cost_center_title")
    private String costCenterTitle;

    @Column(name = "person_count")
    private Integer personCount;

    @Column(name = "average_behavioral")
    private Double averageBehavioral;

    @Column(name = "average_development")
    private Double averageDevelopment;

    @Column(name = "average_operational")
    private Double averageOperational;

    @Column(name = "average_score")
    private Double averageScore;

}

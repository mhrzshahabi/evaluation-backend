package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_evaluation_period")
public class EvaluationPeriod extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evaluation_period_seq")
    @SequenceGenerator(name = "evaluation_period_seq", sequenceName = "seq_evaluation_period_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "c_title")
    private String title;

    @Column(name = "d_start_date")
    private Date startDate;

    @Column(name = "d_end_date")
    private Date endDate;

    @Column(name = "d_start_date_assessment")
    private Date startDateAssessment;

    @Column(name = "d_end_date_assessment")
    private Date endDateAssessment;

    @Column(name = "c_description")
    private String description;
}

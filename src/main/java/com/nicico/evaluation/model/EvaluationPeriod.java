package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

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

    @Column(name = "c_start_date")
    private String startDate;

    @Column(name = "c_end_date")
    private String endDate;

    @Column(name = "c_start_date_assessment")
    private String startDateAssessment;

    @Column(name = "c_end_date_assessment")
    private String endDateAssessment;

    @Column(name = "c_description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluation_period_status_catalog_to_catalog"))
    private Catalog statusCatalog;

    @Column(name = "status_catalog_id", nullable = false)
    private Long statusCatalogId;

}

package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_evaluation")
public class Evaluation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evaluation_seq")
    @SequenceGenerator(name = "evaluation_seq", sequenceName = "evaluation_seq_id", allocationSize = 1)
    private Long id;

    @Column(name = "c_assess_national_code")
    private String assessNationalCode;

    @Column(name = "c_assess_post_code")
    private String assessPostCode;

    @Column(name = "c_assess_full_name")
    private String assessFullName;

    @Column(name = "c_assessor_national_code")
    private String assessorNationalCode;

    @Column(name = "c_assessor_post_code")
    private String assessorPostCode;

    @Column(name = "c_assessor_full_name")
    private String assessorFullName;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluation_method_catalog_to_catalog"))
    private Catalog methodCatalog;

    @NotNull
    @Column(name = "method_catalog_id")
    private Long methodCatalogId;

    @NotNull
    @Column(name = "d_start_date")
    private String startDate;

    @NotNull
    @Column(name = "d_end_date")
    private String endDate;

    @NotNull
    @Column(name = "average_score")
    private Long averageScore;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluation_status_catalog_to_catalog"))
    private Catalog statusCatalog;

    @NotNull
    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_period_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluation_to_evaluation_period"))
    private EvaluationPeriod evaluationPeriod;

    @NotNull
    @Column(name = "evaluation_period_id")
    private Long evaluationPeriodId;





}

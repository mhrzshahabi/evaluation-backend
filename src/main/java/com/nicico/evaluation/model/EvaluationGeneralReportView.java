package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Subselect("select  * from view_evaluation_general_report")
@DiscriminatorValue("viewEvaluationGeneralReport")
public class EvaluationGeneralReportView {

    @Id
    @Column(name = "id")
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

    @Column(name = "average_score")
    private Long averageScore;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false)
    private Catalog statusCatalog;

    @NotNull
    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_period_id", insertable = false, updatable = false, nullable = false)
    private EvaluationPeriod evaluationPeriod;

    @NotNull
    @Column(name = "evaluation_period_id")
    private Long evaluationPeriodId;

    @Column(name = "GHESMAT_TITLE")
    private String ghesmatTitle;

    @Column(name = "MOAVENAT_TITLE")
    private String moavenatTitle;

    @Column(name = "MOJTAMA_TITLE")
    private String mojtamaTitle;

    @Column(name = "OMOOR_TITLE")
    private String omoorTitle;

    @Column(name = "PERSONNEL_CODE")
    private String personnelCode;

    @Column(name = "ASSESSOR_POST_TITLE")
    private String assessorPostTitle;

    @Column(name = "COST_CENTER_CODE")
    private String costCenterCode;

    @Column(name = "COST_CENTER_TITLE")
    private String costCenterTitle;

    @Column(name = "avg_behavioral")
    private String avgBehavioral;

    @Column(name = "weight_behavioral")
    private String weightBehavioral;

    @Column(name = "avg_development")
    private String avgDevelopment;

    @Column(name = "weight_development")
    private String weightDevelopment;

    @Column(name = "avg_operational")
    private String avgOperational;

    @Column(name = "weight_operational")
    private String weightOperational;
}

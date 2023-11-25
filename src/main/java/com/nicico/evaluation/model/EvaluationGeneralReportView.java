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
    @Column(name = "ID")
    private Long id;

    @Column(name = "C_ASSESS_NATIONAL_CODE")
    private String assessNationalCode;

    @Column(name = "C_ASSESS_POST_CODE")
    private String assessPostCode;

    @Column(name = "C_ASSESS_FULL_NAME")
    private String assessFullName;

    @Column(name = "C_ASSESSOR_NATIONAL_CODE")
    private String assessorNationalCode;

    @Column(name = "C_ASSESSOR_POST_CODE")
    private String assessorPostCode;

    @Column(name = "C_ASSESSOR_FULL_NAME")
    private String assessorFullName;

    @Column(name = "AVERAGE_SCORE")
    private Long averageScore;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_CATALOG_ID", insertable = false, updatable = false, nullable = false)
    private Catalog statusCatalog;

    @NotNull
    @Column(name = "STATUS_CATALOG_ID")
    private Long statusCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVALUATION_PERIOD_ID", insertable = false, updatable = false, nullable = false)
    private EvaluationPeriod evaluationPeriod;

    @NotNull
    @Column(name = "EVALUATION_PERIOD_ID")
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

    @Column(name = "AVG_BEHAVIORAL")
    private String avgBehavioral;

    @Column(name = "WEIGHT_BEHAVIORAL")
    private String weightBehavioral;

    @Column(name = "AVG_DEVELOPMENT")
    private String avgDevelopment;

    @Column(name = "WEIGHT_DEVELOPMENT")
    private String weightDevelopment;

    @Column(name = "AVG_OPERATIONAL")
    private String avgOperational;

    @Column(name = "WEIGHT_OPERATIONAL")
    private String weightOperational;

    @Column(name = "COUNTITEM")
    private Long countItem;
}

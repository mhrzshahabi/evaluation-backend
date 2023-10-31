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
@Subselect("select  * from view_evaluation")
@DiscriminatorValue("viewEvaluation")
public class EvaluationView {

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

    @Column(name = "c_description")
    private String description;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluationView_method_catalog_to_catalog"))
    private Catalog methodCatalog;

    @Column(name = "method_catalog_id")
    private Long methodCatalogId;

    @Column(name = "average_score")
    private Long averageScore;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluationView_status_catalog_to_catalog"))
    private Catalog statusCatalog;

    @NotNull
    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_period_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluationView_to_evaluation_period"))
    private EvaluationPeriod evaluationPeriod;

    @NotNull
    @Column(name = "evaluation_period_id")
    private Long evaluationPeriodId;

    @Column(name = "special_case_id")
    private Long specialCaseId;

    @Column(name = "GHESMAT_TITLE")
    private String ghesmatTitle;

    @Column(name = "HOZE_TITLE")
    private String hozeTitle;

    @Column(name = "MOAVENAT_TITLE")
    private String moavenatTitle;

    @Column(name = "MOJTAMA_TITLE")
    private String mojtamaTitle;

    @Column(name = "OMOOR_TITLE")
    private String omoorTitle;

    @Column(name = "POST_CODE")
    private String postCode;

    @Column(name = "POST_GRADE_CODE")
    private String postGradeCode;

    @Column(name = "POST_GRADE_TITLE")
    private String postGradeTitle;

    @Column(name = "POST_GROUP_CODE")
    private String postGroupCode;

    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "POST_TITLE")
    private String postTitle;

    @Column(name = "VAHED_TITLE")
    private String vahedTitle;
}

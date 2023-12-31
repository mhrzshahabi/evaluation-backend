package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_evaluation_item")
public class EvaluationItem extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evaluation_item_seq")
    @SequenceGenerator(name = "evaluation_item_seq", sequenceName = "evaluation_item_seq_id", allocationSize = 1)
    private Long id;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluation_item_to_evaluation"))
    private Evaluation evaluation;

    @NotNull
    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_type_merit_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluation_item_to_group_type_merit"))
    private GroupTypeMerit groupTypeMerit;

    @Column(name = "group_type_merit_id")
    private Long groupTypeMeritId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire_answer_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluation_questionnaire_answer_to_catalog"))
    private Catalog questionnaireAnswerCatalog;

    @Column(name = "questionnaire_answer_catalog_id")
    private Long questionnaireAnswerCatalogId;

    @Column(name = "questionnaire_answer_catalog_value")
    private Double questionnaireAnswerCatalogValue;

    @Column(name = "questionnaire_answer_catalog_code")
    private String questionnaireAnswerCatalogCode;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_merit_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_evaluation_item_to_post_merit"))
    private PostMeritComponent postMeritComponent;

    @Column(name = "post_merit_id")
    private Long postMeritComponentId;

    @Column(name = "c_description")
    private String description;

    @OneToMany(mappedBy = "evaluationItem", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<EvaluationItemInstance> evaluationItemInstance;

    @Setter(AccessLevel.NONE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "merit_component_audit_rev", referencedColumnName = "rev", insertable = false, updatable = false, nullable = false),
            @JoinColumn(name = "merit_component_audit_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    })
    private MeritComponentAudit meritComponentAudit;

    @Column(name = "merit_component_audit_rev")
    private Long meritRev;

    @Column(name = "merit_component_audit_id")
    private Long meritId;
}

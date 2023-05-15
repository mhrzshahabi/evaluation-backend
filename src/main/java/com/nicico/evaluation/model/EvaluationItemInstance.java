package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_evaluation_item_instance")
public class EvaluationItemInstance extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_evaluation_item_instance_id")
    @SequenceGenerator(name = "seq_evaluation_item_instance_id", sequenceName = "seq_evaluation_item_instance_id", allocationSize = 1)
    private Long id;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_merit_instance_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_evaluation_item_instance_to_post_merit_instance"))
    private PostMeritInstance postMeritInstance;

    @NotNull
    @Column(name = "post_merit_instance_id")
    private Long postMeritInstanceId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_group_type_merit_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_evaluation_item_instance_to_instance_group_type_merit"))
    private InstanceGroupTypeMerit instanceGroupTypeMerit;

    @NotNull
    @Column(name = "instance_group_type_merit_id")
    private Long instanceGroupTypeMeritId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_item_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_evaluation_item_instance_to_evaluation_item"))
    private EvaluationItem evaluationItem;

    @NotNull
    @Column(name = "evaluation_item_id")
    private Long evaluationItemId;
}


package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_post_merit_component")
public class PostMeritComponent extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_post_merit_component_id")
    @SequenceGenerator(name = "seq_post_merit_component_id", sequenceName = "seq_post_merit_component_id", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "c_group_post_code")
    private String groupPostCode;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merit_component_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_post_merit_component_to_merit_component"))
    private MeritComponent meritComponent;

    @NotNull
    @Column(name = "merit_component_id")
    private Long meritComponentId;

    @Column(name = "evaluation_item_id")
    private Long evaluationItemId;

    @Min(0)
    @Max(100)
    @NotNull
    @Column(name = "n_weight")
    private Long weight;

}


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
@Table(name = "tbl_post_merit_instance")
public class PostMeritInstance extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_post_merit_instance_id")
    @SequenceGenerator(name = "seq_post_merit_instance_id", sequenceName = "seq_post_merit_instance_id", allocationSize = 1)
    private Long id;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_merit_component_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_post_merit_instance_to_post_merit_component"))
    private PostMeritComponent postMeritComponent;

    @NotNull
    @Column(name = "post_merit_component_id")
    private Long postMeritComponentId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_post_merit_instance_to_instance"))
    private Instance instance;

    @NotNull
    @Column(name = "instance_id")
    private Long instanceId;
}


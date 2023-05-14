package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;
//گروه - نوع - شایستگی

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_group_type_merit")
public class GroupTypeMerit extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_group_type_merit_id")
    @SequenceGenerator(name = "seq_group_type_merit_id", sequenceName = "seq_group_type_merit_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merit_component_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_group_type_merit_to_merit"))
    private MeritComponent meritComponent;

    @NotNull
    @Column(name = "merit_component_id")
    private Long meritComponentId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_type_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_group_type_merit_to_group_type"))
    private GroupType groupType;

    @NotNull
    @Column(name = "group_type_id")
    private Long groupTypeId;

    @NotNull
    @Column(name = "n_weight")
    private Long weight;

    @OneToMany(mappedBy = "groupTypeMerit", fetch = FetchType.LAZY)
    private List<InstanceGroupTypeMerit> instanceGroupTypeMerits;

    @Transient
    private Boolean hasInstance;

    @PostLoad
    public void updateHasInstance() {
        hasInstance = !instanceGroupTypeMerits.isEmpty();
    }

    @Transient
    private String description;

}

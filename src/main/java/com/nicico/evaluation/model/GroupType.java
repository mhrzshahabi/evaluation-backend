package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
// گروه - نوع

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_group_type")
public class GroupType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_type_seq")
    @SequenceGenerator(name = "group_type_seq", sequenceName = "seq_group_type_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "c_code")
    private String code;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpi_type_id", insertable = false, updatable = false,nullable = false, foreignKey = @ForeignKey(name = "f_group_type_to_type"))
    private KPIType kpiType;

    @NotNull
    @Column(name = "kpi_type_id")
    private Long kpiTypeId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false,nullable = false, foreignKey = @ForeignKey(name = "f_group_type_to_group"))
    private Group group;

    @NotNull
    @Column(name = "group_id")
    private Long groupId;

    @Min(1)
    @Max(100)
    @NotNull
    @Column(name = "n_weight")
    private Long weight;

    @OneToMany(mappedBy = "groupType", fetch = FetchType.LAZY)
    private List<GroupTypeMerit> groupTypeMeritList;

}

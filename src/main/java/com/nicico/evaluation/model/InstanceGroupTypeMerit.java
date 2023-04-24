package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
//مصداق- گروه - نوع - شایستگی
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_instance_group_type_merit")
public class InstanceGroupTypeMerit extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_instance_group_type_merit_id")
    @SequenceGenerator(name = "seq_instance_group_type_merit_id", sequenceName = "seq_instance_group_type_merit_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_type_merit_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_instance_group_type_merit_to_group_type_merit"))
    private GroupTypeMerit groupTypeMerit;

    @Column(name = "group_type_merit_id")
    private Long groupTypeMeritId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_instance_group_type_merit_to_instance"))
    private Instance instance;

    @Column(name = "instance_id")
    private Long instanceId;

}

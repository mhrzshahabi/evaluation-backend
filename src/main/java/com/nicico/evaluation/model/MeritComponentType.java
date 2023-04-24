package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
//شایستگی - نوع
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_merit_component_type")
public class MeritComponentType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_merit_component_type_id")
    @SequenceGenerator(name = "seq_merit_component_type_id", sequenceName = "seq_merit_component_type_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpi_type_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_merit_component_type_to_type"))
    private KPIType kpiType;

    @Column(name = "kpi_type_id")
    private Long kpiTypeId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merit_component_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_merit_component_type_to_merit_component"))
    private MeritComponent meritComponent;


    @Column(name = "merit_component_id")
    private Long meritComponentId;
}

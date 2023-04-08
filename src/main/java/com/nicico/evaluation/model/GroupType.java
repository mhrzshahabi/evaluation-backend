package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tbl_group_type")
@Data
@NoArgsConstructor
public class GroupType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_type_seq")
    @SequenceGenerator(name = "group_type_seq", sequenceName = "seq_group_type_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;
    
    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpi_type_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_group_type_to_type"))
    private KPIType kpiType;

    @Column(name = "kpi_type_id")
    private Long kpiTypeId;   
     
    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_group_type_to_group"))
    private Group group;

    @Column(name = "group_id")
    private Long groupId;

    @NotNull
    @Column(name = "n_weight")
    private Long weight;

}

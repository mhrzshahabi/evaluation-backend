package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "TBL_KPI_TYPE")
public class KPIType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kpi_type_seq")
    @SequenceGenerator(name = "kpi_type_seq", sequenceName = "seq_kpi_type_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "c_code")
    private String code;

    @NotNull
    @Column(name = "c_title")
    private String title;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_def_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_type_to_catalog"))
    private Catalog levelDefCatalog;

    @NotNull
    @Column(name = "level_def_id")
    private Long levelDefCatalogId;

}

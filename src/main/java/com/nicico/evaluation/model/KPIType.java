package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TBL_KPI_TYPE")
@Data
@NoArgsConstructor
public class KPIType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "kpi_type_seq")
    @SequenceGenerator(name = "kpi_type_seq", sequenceName = "SEQ_KPI_TYPE_ID", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "c_code")
    private String code;

    @Column(name = "c_title")
    private String title;

    @Column(name = "n_level_def")
    private Long levelDef;

}

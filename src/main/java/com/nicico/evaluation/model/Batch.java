package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_batch")
public class Batch extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_batch_id")
    @SequenceGenerator(name = "seq_batch_id", sequenceName = "seq_batch_id", allocationSize = 1)
    private Long id;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_batch_title_to_catalog"))
    private Catalog titleCatalog;

    @NotNull
    @Column(name = "title_catalog_id")
    private Long titleCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_batch_status_to_catalog"))
    private Catalog statusCatalog;

    @NotNull
    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Transient
    private Integer total;

    @Transient
    private Integer successfulNumber;

    @Transient
    private Integer failedNumber;

}

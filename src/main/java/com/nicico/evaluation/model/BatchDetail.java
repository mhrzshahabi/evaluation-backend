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
@Table(name = "tbl_batch_detail")
public class BatchDetail extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_batch_detail_id")
    @SequenceGenerator(name = "seq_batch_detail_id", sequenceName = "seq_batch_detail_id", allocationSize = 1)
    private Long id;

    @Column(name = "c_description")
    private String description;

    @Column(name = "c_exception_title")
    private String exceptionTitle;

    @Column(name = "c_input_dto")
    private String inputDTO;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_batch_detail_status_to_catalog"))
    private Catalog statusCatalog;

    @NotNull
    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_batch_detail_to_batch"))
    private Batch batch;

    @NotNull
    @Column(name = "batch_id")
    private Long batchId;

}

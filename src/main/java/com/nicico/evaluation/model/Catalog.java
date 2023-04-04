package com.nicico.evaluation.model;

import com.nicico.evaluation.common.EvaluationAudit;
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
@Table(name = "tbl_catalog", uniqueConstraints = {@UniqueConstraint(columnNames = {"f_catalog_type_id", "c_title", "c_code"})})
public class Catalog extends EvaluationAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_catalog_id")
    @SequenceGenerator(name = "seq_catalog_id", sequenceName = "seq_catalog_id", allocationSize = 1)
    private Long id;

    @Column(name = "c_title", nullable = false, unique = true)
    private String title;

    @Column(name = "c_code", nullable = false, unique = true)
    private String code;

    @Column(name = "c_description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_catalog_type_id", insertable = false, updatable = false)
    private CatalogType catalogType;

    @Column(name = "f_catalog_type_id")
    private Long catalogTypeId;
}

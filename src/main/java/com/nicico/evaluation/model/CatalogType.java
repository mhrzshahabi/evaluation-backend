package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_catalog_type")
public class CatalogType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_catalog_type_id")
    @SequenceGenerator(name = "seq_catalog_type_id", sequenceName = "seq_catalog_type_id", allocationSize = 1)
    private Long id;

    @Column(name = "c_title", nullable = false, unique = true)
    private String title;

    @Column(name = "c_code", nullable = false, unique = true)
    private String code;

    @Column(name = "c_description")
    private String description;

    @OneToMany(mappedBy = "catalogType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Catalog> catalogList;

}

package com.nicico.evaluation.model;

import com.nicico.evaluation.model.compositeKey.MeritComponentAuditKey;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Subselect("select * from tbl_merit_component_aud")
@DiscriminatorValue("MeritComponentAudit")
public class MeritComponentAudit implements Serializable {

    @EmbeddedId
    private MeritComponentAuditKey auditId;

    @Column(name = "id", insertable = false, updatable = false, precision = 10)
    private Long id;

    @Column(name = "rev", insertable = false, updatable = false, precision = 10)
    private Long rev;

    @Column(name = "c_title")
    private String title;

    @Column(name = "c_code")
    private String code;

    @OneToMany(fetch = FetchType.LAZY)
    private List<MeritComponentType> meritComponentTypes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false)
    private Catalog statusCatalog;

    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Column(name = "c_description")
    private String description;
}

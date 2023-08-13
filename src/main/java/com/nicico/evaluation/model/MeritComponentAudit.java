package com.nicico.evaluation.model;

import com.nicico.evaluation.model.compositeKey.MeritComponentAuditKey;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;

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

    @Column(name = "c_title")
    private String title;

    @Column(name = "c_code")
    private String code;

    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Column(name = "c_description")
    private String description;
}

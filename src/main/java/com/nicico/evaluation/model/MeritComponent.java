package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

//شایستگی
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_merit_component", uniqueConstraints = {@UniqueConstraint(columnNames = {"c_title", "c_code"})})
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditOverride(forClass = Auditable.class)
public class MeritComponent extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_merit_component_id")
    @SequenceGenerator(name = "seq_merit_component_id", sequenceName = "seq_merit_component_id", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "c_title", nullable = false, unique = true)
    private String title;

    @NotNull
    @Column(name = "c_code", nullable = false, unique = true)
    private String code;

    @NotAudited
    @OneToMany(mappedBy = "meritComponent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MeritComponentType> meritComponentTypes;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_merit_component_status_to_catalog"))
    private Catalog statusCatalog;

    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Column(name = "c_description")
    private String description;
}

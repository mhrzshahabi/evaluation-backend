package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_sensitive_events")
public class SensitiveEvents extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sensitive_events_seq")
    @SequenceGenerator(name = "sensitive_events_seq", sequenceName = "seq_sensitive_events_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "c_title")
    private String title;

    @NotNull
    @Column(name = "d_event_date")
    private Date eventDate;

    @NotNull
    @Column(name = "d_to_date")
    private Date toDate;

    @NotNull
    @Column(name = "n_level_effect")
    private Long levelEffect;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_event_type_to_catalog"))
    private Catalog typeCatalog;

    @NotNull
    @Column(name = "type_catalog_id")
    private Long typeCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_policy_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_event_policy_status_to_catalog"))
    private Catalog eventPolicyCatalog;

    @NotNull
    @Column(name = "event_policy_catalog_id")
    private Long eventPolicyCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_sensitive_events_status_to_catalog"))
    private Catalog statusCatalog;

    @NotNull
    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Column(name = "c_description", length = 2000)
    private String description;
}

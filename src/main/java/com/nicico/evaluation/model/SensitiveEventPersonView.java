package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Subselect("select  * from view_sensitive_event_person")
@DiscriminatorValue("viewSensitiveEventPerson")
public class SensitiveEventPersonView {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "c_title")
    private String title;

    @Column(name = "d_event_date")
    private String eventDate;

    @Column(name = "d_to_date")
    private String toDate;

    @Column(name = "n_level_effect")
    private Long levelEffect;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_sensitive_event_person_type_to_catalog"))
    private Catalog typeCatalog;

    @Column(name = "type_catalog_id")
    private Long typeCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_policy_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_sensitive_event_person_policy_status_to_catalog"))
    private Catalog eventPolicyCatalog;

    @Column(name = "event_policy_catalog_id")
    private Long eventPolicyCatalogId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_sensitive_event_person_status_to_catalog"))
    private Catalog statusCatalog;

    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Column(name = "c_description", length = 2000)
    private String description;

    @Column(name = "creator_national_code")
    private String creatorNationalCode;

    @Column(name = "sensitive_event_person_id")
    private Long sensitiveEventPersonId;

    @Column(name = "c_national_code")
    private String nationalCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "personnel_code")
    private String personnelCode;

}

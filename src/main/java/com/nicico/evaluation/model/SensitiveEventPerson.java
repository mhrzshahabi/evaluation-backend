package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_sensitive_event_person")
public class SensitiveEventPerson extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sensitive_event_person_seq")
    @SequenceGenerator(name = "sensitive_event_person_seq", sequenceName = "sensitive_event_person_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "c_national_code")
    private String nationalCode;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensitive_event_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_sensitive_event_person_to_sensitive_event"))
    private SensitiveEvents sensitiveEvent;

    @NotNull
    @Column(name = "sensitive_event_id")
    private Long sensitiveEventId;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merit_component_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_sensitive_event_person_to_merit_component"))
    private MeritComponent meritComponent;

    @NotNull
    @Column(name = "merit_component_id")
    private Long meritComponentId;

    @NotNull
    @Column(name = "n_participation")
    private Long participation;
}

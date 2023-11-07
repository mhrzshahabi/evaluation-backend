package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @NotNull
    @Column(name = "n_level_effect")
    private Long levelEffect;

}

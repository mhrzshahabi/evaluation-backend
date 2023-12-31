package com.nicico.evaluation.model.compositeKey;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Access(AccessType.FIELD)
@EqualsAndHashCode(of = {"id", "rev"}, callSuper = false)
@Embeddable
public class MeritComponentAuditKey implements Serializable {

    @Column(name = "id")
    private Long id;

    @Column(name = "rev")
    private Long rev;
}


package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_attachment")
public class Attachment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_seq")
    @SequenceGenerator(name = "attachment_seq", sequenceName = "seq_attachment_id", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "c_fms_key")
    private String fmsKey;

    @Any(metaColumn = @Column(name = "c_object_type", nullable = false), fetch = FetchType.LAZY)
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {
                    @MetaValue(value = "SensitiveEvents", targetEntity = SensitiveEvents.class)
            })
    @JoinColumn(name = "f_object", nullable = false, insertable = false, updatable = false)
    private Object object;

    @Column(name = "f_object", nullable = false)
    private Long objectId;

    @NotNull
    @Column(name = "c_group_id")
    private String groupId;


}

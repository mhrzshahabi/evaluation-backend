package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import com.nicico.evaluation.utility.EvaluationConstant;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "tbl_attachment",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"c_object_type", "f_object_id", "c_file_name"})})
public class Attachment<E> extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_seq")
    @SequenceGenerator(name = "attachment_seq", sequenceName = "seq_attachment_id", allocationSize = 1)
    @Column(name = "id", precision = 10)
    private Long id;

    @NotNull
    @Column(name = "c_fms_key")
    private String fmsKey;

    @Column(name = "c_file_name", nullable = false)
    private String fileName;

    @Column(name = "f_file_type_id", nullable = false)
    private Integer fileTypeId;

    @Column(name = "c_description", length = 500)
    private String description;

    @Any(
            metaColumn = @Column(name = "c_object_type", nullable = false, length = 30),
            fetch = FetchType.LAZY
    )
    @AnyMetaDef(
            idType = "long", metaType = "string",
            metaValues = {
                    @MetaValue(targetEntity = SensitiveEvents.class, value = EvaluationConstant.SENSITIVE_EVENTS),
                    @MetaValue(targetEntity = Batch.class, value = EvaluationConstant.BATCH),
            }
    )
    @JoinColumn(name = "f_object_id", nullable = false, insertable = false, updatable = false)
    private E object;

    @Column(name = "c_object_type")
    private String objectType;

    @Column(name = "f_object_id", nullable = false)
    private Long objectId;

    @NotNull
    @Column(name = "c_group_id")
    private String groupId;


}

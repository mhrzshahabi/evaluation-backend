package com.nicico.evaluation.equipment.standard_test_status;
import com.nicico.evaluation.common.EvaluationAudit;
import lombok.*;
import lombok.experimental.Accessors;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_standardTestsStatus")
public class StandardTestStatus extends EvaluationAudit {
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "standardTestStatus_seq")
@SequenceGenerator(name = "standardTestStatus_seq", sequenceName = "seq_standardTestStatus_id", allocationSize = 1)
private Long id;
    @Column( name="C_title",unique=true)
    @NotNull(message = "Should not be null")
private String title;
    @Column( name="C_title_color")
    @NotNull(message = "Should not be null")
    private String titleColor;
}



package com.nicico.evaluation.equipment.standard_test;

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
@EqualsAndHashCode( callSuper = false)

@Entity
@Table(name = "tbl_standardTests")
public class StandardTest extends EvaluationAudit {




@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "standardTest_seq")
@SequenceGenerator(name = "standardTest_seq", sequenceName = "seq_standardTest_id", allocationSize = 1)
private Long id;


    @Column( name="C_title",unique=true)
    @NotNull(message = "Should not be null")
private String title;





}

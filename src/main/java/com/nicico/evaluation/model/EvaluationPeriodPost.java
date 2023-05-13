package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_evaluation_period_post")
public class EvaluationPeriodPost extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evaluation_period_post_seq")
    @SequenceGenerator(name = "evaluation_period_post_seq", sequenceName = "seq_evaluation_period_post_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_period_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "f_evaluation_period_id_to_evaluation_period"))
    private EvaluationPeriod evaluationPeriod;

    @Column(name = "evaluation_period_id")
    private Long evaluationPeriodId;

    @Column(name = "c_post_code")
    private String postCode;
}

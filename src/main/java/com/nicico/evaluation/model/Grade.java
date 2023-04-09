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
@Subselect("select  * from syn_hrm_view_grade")
@DiscriminatorValue("synHrmViewGrade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_grade_id")
    @SequenceGenerator(name = "seq_grade_id", sequenceName = "seq_grade_id", allocationSize = 1)
    private Long id;

    @Column(name = "C_POST_GRADE_CODE")
    private String code;

    @Column(name = "C_POST_GRADE_TITLE")
    private String title;

}

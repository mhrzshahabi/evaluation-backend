package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
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
@Subselect("select  hrm_tbl_post_grade.id,hrm_tbl_post_grade.c_title,hrm_tbl_post_grade.c_code from hrm_tbl_post_grade")
@DiscriminatorValue("hrmTblPostGrade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grade_id_seq")
    @SequenceGenerator(name = "grade_id_seq", sequenceName = "seq_grade_id", allocationSize = 1)
    private Long id;

    @Column(name = "c_code")
    private String code;


    @Column(name = "c_title")
    private String title;


}

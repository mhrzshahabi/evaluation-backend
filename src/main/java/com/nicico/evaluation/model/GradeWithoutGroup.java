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
@Subselect("select * from view_unused_grade")
@DiscriminatorValue("GradeWithoutGroup")
public class GradeWithoutGroup {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "C_POST_GRADE_CODE")
    private String code;

    @Column(name = "C_POST_GRADE_TITLE")
    private String title;

}

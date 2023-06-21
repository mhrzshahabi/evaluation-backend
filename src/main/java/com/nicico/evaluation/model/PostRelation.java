package com.nicico.evaluation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Subselect("select * from view_post_relation")
@DiscriminatorValue("viewPostRelation")
public class PostRelation {
    @Id
    private Long id;
    @Column(name = "post_code")
    private String postCode;
    @Column(name = "post_title")
    private String postTitle;
    @Column(name = "post_group_code")
    private String postGroupCode;
    @Column(name = "post_code_parent")
    private String postCodeParent;
    @Column(name = "post_title_parent")
    private String postTitleParent;
    @Column(name = "post_code_grade")
    private String postCodeGrade;
    @Column(name = "post_title_grade")
    private String postTitleGrade;
    @Column(name = "post_code_grade_parent")
    private String postCodeGradeParent;
    @Column(name = "post_title_grade_parent")
    private String postTitleGradeParent;
}

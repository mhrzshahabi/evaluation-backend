package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

//پست گروهی (بدون اسلش)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Subselect("select * from view_group_post")
@DiscriminatorValue("viewGroupPost")
public class GroupPost {

    @Id
    private Long id;

    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "POST_GRADE_ID")
    private Long postGradeId;

    @Column(name = "POST_GRADE_CODE")
    private String postGradeCode;

    @Column(name = "POST_GRADE_TITLE")
    private String postGradeTitle;

    @Column(name = "POST_GRADE_CODE_PARENT")
    private String postGradeCodeParent;

    @Column(name = "POST_GRADE_TITLE_PARENT ")
    private String postGradeTitleParent;

    @Column(name = "GROUP_POST_CODE")
    private String groupPostCode;

    @Column(name = "POST_TITLE")
    private String postTitle;

    @Column(name = "POST_PARENT_ID")
    private Long postParentId;

    @Column(name = "POST_LEVEL")
    private Long postLevel;

    @Column(name = "POST_FULL_PATH_CODE")
    private String postGullPathCode;

    @Column(name = "POST_MOSAVAB")
    private Long postMosavab;

    @Column(name = "POST_COMPANY_ID")
    private Long postCompanyId;

    @Column(name = "POST_COMPANY_CODE")
    private String postCompanyCode;

    @Column(name = "POST_COMPANY_NAME")
    private String postCompanyName;

    @Column(name = "COST_CENTER_ID")
    private Long costCenterId;

    @Column(name = "COST_CENTER_CODE")
    private String costCenterCode;

    @Column(name = "COST_CENTER_TITLE")
    private String costCenterTitle;

    @Column(name = "HOZE_CODE")
    private String hozeCode;

    @Column(name = "HOZE_TITLE")
    private String hozeTitle;

    @Column(name = "MOJTAMA_CODE")
    private String mojtamaCode;

    @Column(name = "MOJTAMA_TITLE")
    private String mojtamaTitle;

    @Column(name = "OMOOR_CODE")
    private String omoorCode;

    @Column(name = "OMOOR_TITLE")
    private String omoorTitle;

    @Column(name = "MOAVENAT_CODE")
    private String moavenatCode;

    @Column(name = "MOAVENAT_TITLE")
    private String moavenatTitle;

    @Column(name = "GHESMAT_CODE")
    private String ghesmatCode;

    @Column(name = "GHESMAT_TITLE")
    private String ghesmatTitle;

    @Column(name = "VAHED_CODE")
    private String vahedCode;

    @Column(name = "VAHED_TITLE")
    private String vahedTitle;

}

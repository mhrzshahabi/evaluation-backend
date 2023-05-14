package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Subselect("select * from view_organization_tree")
@DiscriminatorValue("viewOrganizationTree")
public class OrganizationTree {
    @Id
    private Long id;
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "org_structure_id")
    private Long orgStructureId;
    @Column(name = "company_id")
    private Long companyId;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "org_structure_name")
    private String orgStructureName;
    @Column(name = "d_start_date")
    private Date startDate;
    @Column(name = "d_end_date")
    private Date endDate;
    @Column(name = "post_parent_id")
    private Long postParentId;
    @Column(name = "post_code")
    private String postCode;
    @Column(name = "post_title")
    private String postTitle;
    @Column(name = "post_level")
    private Long postLevel;
    @Column(name = "post_path")
    private String postPath;
    @Column(name = "post_group_code")
    private String postGroupCode;
    @Column(name = "end_date_post")
    private Date endDatePost;
    @Column(name = "effective_date")
    private Date effectiveDate;
    @Column(name = "start_date_post")
    private Date startDatePost;
    @Column(name = "post_status_id")
    private Long postStatusId;
    @Column(name = "post_status_name")
    private String postStatusName;
    @Column(name = "post_department_id")
    private Long postDepartmentId;
    @Column(name = "full_path_post_id")
    private String fullPathPostId;
    @Column(name = "personnel_id")
    private Long personnelId;
    @Column(name = "person_id")
    private Long personId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "national_code")
    private String nationalCode;
    @Column(name = "expire_date_en")
    private Date expireDateEn;
    @Column(name = "expire_date_fa")
    private String expireDateFa;
    @Column(name = "today")
    private String today;
    @Column(name = "department_id")
    private String departmentId;
    @Column(name = "department_code")
    private String departmentCode;
    @Column(name = "department_name")
    private String departmentName;
    @Column(name = "POST_PARENT_CODE")
    private String postParentCode;
    @Column(name = "POST_PARENT_TITLE")
    private String postParentTitle;
    @Column(name = "FIRST_NAME_PARENT")
    private String firstNameParent;
    @Column(name = "LAST_NAME_PARENT")
    private String lastNameParent;
    @Column(name = "NATIONAL_CODE_PARENT")
    private String nationalCodeParent;

}

package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class OrganizationTreeDTO {

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationTreeInfo")
    public static class Info {
        private Long id;
        private Long postId;
        private Long orgStructureId;
        private Long companyId;
        private String companyName;
        private String orgStructureName;
        private Date startDate;
        private Date endDate;
        private Long postParentId;
        private String postCode;
        private String postTitle;
        private Long postLevel;
        private String postPath;
        private String postGroupCode;
        private Date endDatePost;
        private Date effectiveDate;
        private Date startDatePost;
        private Long postStatusId;
        private String postStatusName;
        private Long postDepartmentId;
        private String fullPathPostId;
        private Long personnelId;
        private Long personId;
        private String firstName;
        private String lastName;
        private String fullName;
        private String nationalCode;
        private Date expireDateEn;
        private String expireDateFa;
        private String today;
        private String departmentId;
        private String departmentCode;
        private String departmentName;
        private String postParentCode;
        private String postParentTitle;
        private String firstNameParent;
        private String lastNameParent;
        private String nationalCodeParent;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationTreeInfoDetail")
    public static class InfoDetail {
        private Long id;
        private String postTitle;
        private String postGroupCode;
        private String postCode;
        private Long postLevel;
        private String postStatusName;
        private String departmentName;
        private Date startDatePost;
        private Date effectiveDate;
        private Date endDatePost;
        private String postParentTitle;
        private String postParentCode;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationTreeInfoTree")
    public static class InfoTree {
        private Long id;
        private Long postId;
        private Long postParentId;
        private String nameFa;
        private Long childNode;
        private String postPath;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationTreeSearchTree")
    public static class SearchTree {
        private String nameFa;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationTreeSpecResponse")
    public static class SpecResponse {
        private OrganizationTreeDTO.Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationTreeResponse")
    public static class Response {
        private List<OrganizationTreeDTO.Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}

package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class OrganizationDTO {

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationInfo")
    public static class Info {
        private Long id;
        private Long companyId;
        private String companyName;
        private Long orgStructureId;
        private String orgStructureName;
        private String startDate;
        private String endDate;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationSpecResponse")
    public static class SpecResponse {
        private OrganizationDTO.Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("OrganizationResponse")
    public static class Response {
        private List<OrganizationDTO.Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}

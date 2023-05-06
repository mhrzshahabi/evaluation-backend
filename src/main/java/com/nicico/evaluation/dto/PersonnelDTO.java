package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class PersonnelDTO {

    @Getter
    @Setter
    @ApiModel("PersonnelInfo")
    public static class Info extends PersonnelDTO {

        private Long id;
        private Long personelId;
        private Long personId;
        private String nationalCode;
        private String fullName;
        private String fatherName;
        private String personelNo;
        private String contractNumber;
        private Long departmentCostCenter;
        private String depCode;
        private String deptTitle;
        private String rewardDepCode;
        private String rewardDepTitle;
        private Long personel;
        private String depGeoCode;
        private String depGeoGhesmatCode;
        private String depGeoGhesmatTitle;
        private String depGeoHozeCode;
        private String depGeoHozeTitle;
        private String depGeoMoavenatCode;
        private String depGeoMoavenatTitle;
        private String depGeoMojtameCode;
        private String depGeoMojtameTitle;
        private String depGeoOmorCode;
        private String depGeoOmorTitle;
        private String companyCode;
        private String companyoName;
        private Long personelContractTypeId;
        private String contractTypeName;
        private Long employmentModeId;
        private String employmentModeName;
        private Long employmentTypeId;
        private String employmentTypeName;
        private Long entranceTypeId;
        private String entranceTypeName;
        private Long geoWorkId;
        private String geoWorkName;
        private Long operationsUnitId;
        private String operationsUnitName;
        private Long terminationContractId;
        private String terminationContractName;
        private Long workTurnId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PersonnelSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PersonnelResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}

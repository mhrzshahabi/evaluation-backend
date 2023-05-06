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
public abstract class PersonDTO {

    @Getter
    @Setter
    @ApiModel("PersonInfo")
    public static class Info extends PersonDTO {

        private Long id;
        private Long personId;
        private Long personelId;
        private String firstName;
        private String lastName;
        private String nationalCode;
        private String fatherName;
        private String motherName;
        private Date birthDate;
        private Long genderId;
        private String genderName;
        private String mobileNumber;
        private String email;
        private String birthCertificateNumber;
        private String birthCertificateSeries;
        private String birtCertificateSerial;
        private Long areaCityId;
        private String areaCityName;
        private Long birthAreaCityId;
        private String birthAreaCityName;
        private Long nationalityId;
        private String nationalityName;
        private Long religionId;
        private String religionName;
        private Long taholStatusId;
        private String taholStatusName;
        private Long militaryId;
        private String militaryName;
        private Long bloodTypenId;
        private String bloodTypenName;
        private Date deathDate;
        private Long deathCauseId;
        private String deathDescription;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PersonSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PersonResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}

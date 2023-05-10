package com.nicico.evaluation.model;

import lombok.*;
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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Subselect("select * from view_personnel")
@DiscriminatorValue("viewPersonnel")
public class Personnel {

    @Id
    private Long id;

    @Column(name = "PERSONEL_ID")
    private Long personelId;

    @Column(name = "PERSON_ID")
    private Long personId;

    @Column(name = "NATIONAL_CODE")
    private String nationalCode;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "FATHER_NAME")
    private String fatherName;

    @Column(name = "PERSONEL_NO")
    private String personelNo;

    @Column(name = "CONTRACT_NUMBER")
    private String contractNumber;

    @Column(name = "DEPARTMENT_COST_CENTER")
    private Long departmentCostCenter;

    @Column(name = "DEP_CODE")
    private String depCode;

    @Column(name = "DEP_TITLE")
    private String deptTitle;

    @Column(name = "REWARD_DEP_CODE")
    private String rewardDepCode;

    @Column(name = "REWARD_DEP_TITLE")
    private String rewardDepTitle;

    @Column(name = "PERSONEL")
    private Long personel;

    @Column(name = "DEP_GEO_CODE")
    private String depGeoCode;

    @Column(name = "DEP_GEO_GHESMAT_CODE")
    private String depGeoGhesmatCode;

    @Column(name = "DEP_GEO_GHESMAT_TITLE")
    private String depGeoGhesmatTitle;

    @Column(name = "DEP_GEO_HOZE_CODE")
    private String depGeoHozeCode;

    @Column(name = "DEP_GEO_HOZE_TITLE")
    private String depGeoHozeTitle;

    @Column(name = "DEP_GEO_MOAVENAT_CODE")
    private String depGeoMoavenatCode;

    @Column(name = "DEP_GEO_MOAVENAT_TITLE")
    private String depGeoMoavenatTitle;

    @Column(name = "DEP_GEO_MOJTAME_CODE")
    private String depGeoMojtameCode;

    @Column(name = "DEP_GEO_MOJTAME_TITLE")
    private String depGeoMojtameTitle;

    @Column(name = "DEP_GEO_OMOR_CODE")
    private String depGeoOmorCode;

    @Column(name = "DEP_GEO_OMOR_TITLE")
    private String depGeoOmorTitle;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "COMPANYO_NAME")
    private String companyoName;

    @Column(name = "PERSONEL_CONTRACT_TYPE_ID")
    private Long personelContractTypeId;

    @Column(name = "CONTRACT_TYPE_NAME")
    private String contractTypeName;

    @Column(name = "EMPLOYMENT_MODE_ID")
    private Long employmentModeId;

    @Column(name = "EMPLOYMENT_MODE_NAME")
    private String employmentModeName;

    @Column(name = "EMPLOYMENT_TYPE_ID")
    private Long employmentTypeId;

    @Column(name = "EMPLOYMENT_TYPE_NAME")
    private String employmentTypeName;

    @Column(name = "ENTRANCE_TYPE_ID")
    private Long entranceTypeId;

    @Column(name = "ENTRANCE_TYPE_NAME")
    private String entranceTypeName;

    @Column(name = "GEO_WORK_ID")
    private Long geoWorkId;

    @Column(name = "GEO_WORK_NAME")
    private String geoWorkName;

    @Column(name = "OPERATIONS_UNIT_ID")
    private Long operationsUnitId;

    @Column(name = "OPERATIONS_UNIT_NAME")
    private String operationsUnitName;

    @Column(name = "TERMINATION_CONTRACT_ID")
    private Long terminationContractId;

    @Column(name = "TERMINATION_CONTRACT_NAME")
    private String terminationContractName;

    @Column(name = "WORK_TURN_ID")
    private Long workTurnId;

    @Column(name = "POST_CODE")
    private String postCode;

    @Column(name = "POST_TITLE")
    private String postTitle;

}

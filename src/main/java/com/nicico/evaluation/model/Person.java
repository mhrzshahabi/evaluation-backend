package com.nicico.evaluation.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Subselect("select * from view_person")
@DiscriminatorValue("viewPerson")
public class Person {

    @Id
    private Long id;

    @Column(name = "PERSON_ID")
    private Long personId;

    @Column(name = "PERSONEL_ID")
    private Long personelId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "NATIONAL_CODE")
    private String nationalCode;

    @Column(name = "FATHER_NAME")
    private String fatherName;

    @Column(name = "MOTHER_NAME")
    private String motherName;

    @Column(name = "BIRTH_DATE")
    private Date birthDate;

    @Column(name = "GENDER_ID")
    private Long genderId;

    @Column(name = "GENDER_NAME")
    private String genderName;

    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "BIRTH_CERTIFICATE_NUMBER")
    private String birthCertificateNumber;

    @Column(name = "BIRTH_CERTIFICATE_SERIES")
    private String birthCertificateSeries;

    @Column(name = "BIRTH_CERTIFICATE_SERIAL")
    private String birtCertificateSerial;

    @Column(name = "AREA_CITY_ID")
    private Long areaCityId;

    @Column(name = "AREA_CITY_NAME")
    private String areaCityName;

    @Column(name = "BIRTH_AREA_CITY_ID")
    private Long birthAreaCityId;

    @Column(name = "BIRTH_AREA_CITY_NAME")
    private String birthAreaCityName;

    @Column(name = "NATIONALITY_ID")
    private Long nationalityId;

    @Column(name = "NATIONALITY_NAME")
    private String nationalityName;

    @Column(name = "RELIGION_ID")
    private Long religionId;

    @Column(name = "RELIGION_NAME")
    private String religionName;

    @Column(name = "TAHOL_STATUS_ID")
    private Long taholStatusId;

    @Column(name = "TAHOL_STATUS_NAME")
    private String taholStatusName;

    @Column(name = "MILITARY_ID")
    private Long militaryId;

    @Column(name = "MILITARY_NAME")
    private String militaryName;

    @Column(name = "BLOOD_TYPEN_ID")
    private Long bloodTypenId;

    @Column(name = "BLOOD_TYPEN_NAME")
    private String bloodTypenName;

    @Column(name = "DEATH_DATE")
    private Date deathDate;

    @Column(name = "DEATH_CAUSE_ID")
    private Long deathCauseId;

    @Column(name = "DEATH_DESCRIPTION")
    private String deathDescription;

    @Transient
    private String fullName;

    @PostLoad
    public void updateStatistics() {
        fullName = firstName + " " + lastName;
    }
}
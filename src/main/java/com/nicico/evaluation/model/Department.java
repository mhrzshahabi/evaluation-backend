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
@Subselect("select * from view_department")
@DiscriminatorValue("viewDepartment")
public class Department {

    @Id
    private Long id;

    @Column(name = "C_TITLE")
    private String title;

    @Column(name = "C_MOJTAME_CODE")
    private String mojtameCode;

    @Column(name = "C_MOJTAME_TITLE")
    private String mojtameTitle;

    @Column(name = "C_MOAVENAT_CODE")
    private String moavenatCode;

    @Column(name = "C_MOAVENAT_TITLE")
    private String moavenatTitle;

    @Column(name = "C_OMOR_CODE")
    private String omorCode;

    @Column(name = "C_OMOR_TITLE")
    private String omorTitle;

    @Column(name = "C_GHESMAT_CODE")
    private String ghesmatCode;

    @Column(name = "C_GHESMAT_TITLE")
    private String ghesmatTitle;

}

package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_specialcase")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialCase extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_case_seq")
    @SequenceGenerator(name = "special_case_seq", sequenceName = "seq_special_case_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "c_firstName")
    private String firstName;

    @Column(name = "c_lastName")
    private String lastName;

    @Column(name = "c_nationalCode")
    private String nationalCode;

    @Column(name = "c_postCode")
    private String postCode;

    @Column(name = "c_assessorName")
    private String assessorName;

    @Column(name = "c_assessorNationalCode")
    private String assessorNationalCode;

    @Column(name = "c_assessorPostCode")
    private String assessorPostCode;

    @Column(name = "d_startDate")
    private Date startDate;

    @Column(name = "d_endDate")
    private Date endDate;

}
    
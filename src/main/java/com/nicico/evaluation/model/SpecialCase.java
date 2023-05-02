
package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_special_case")
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

    @Column(name = "c_first_name")
    private String firstName;

    @Column(name = "c_last_name")
    private String lastName;

    @Column(name = "c_national_code")
    private String nationalCode;

    @Column(name = "c_post_code")
    private String postCode;

    @Column(name = "c_assessor_name")
    private String assessorName;

    @Column(name = "c_assessor_national_code")
    private String assessorNationalCode;

    @Column(name = "c_assessor_post_code")
    private String assessorPostCode;

    @Column(name = "d_start_date")
    private Date startDate;

    @Column(name = "d_end_date")
    private Date endDate;

}
    
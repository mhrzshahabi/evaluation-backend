
package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "c_assess_full_name")
    private String assessFullName;

    @Column(name = "c_assess_national_code")
    private String assessNationalCode;

    @Column(name = "c_assess_post_code")
    private String assessPostCode;

    @Column(name = "c_assess_real_post_code")
    private String assessRealPostCode;

    @Column(name = "c_assessor_full_name")
    private String assessorFullName;

    @Column(name = "c_assessor_national_code")
    private String assessorNationalCode;

    @Column(name = "c_assessor_post_code")
    private String assessorPostCode;

    @Column(name = "c_start_date")
    private String startDate;

    @Column(name = "c_end_date")
    private String endDate;

}
    